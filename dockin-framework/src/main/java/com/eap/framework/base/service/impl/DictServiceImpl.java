package com.eap.framework.base.service.impl;

import com.alibaba.fastjson.JSON;
import com.eap.framework.base.entity.Dict;
import com.eap.framework.base.pojo.Result;
import com.eap.framework.base.pojo.TreeNode;
import com.eap.framework.base.service.CodeGeneratorService;
import com.eap.framework.base.service.DictService;
import com.eap.framework.constant.RedisConstant;
import com.eap.framework.utils.CodeUtil;
import com.eap.framework.utils.PingYinUtil;
import com.eap.framework.utils.StrUtil;
import com.eap.framework.utils.TreeUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service("dictService")
public class DictServiceImpl extends BaseServiceImpl implements DictService {

    @Resource
    private CodeGeneratorService codeGeneratorService;

    @Override
    public List<TreeNode> getTreeData() {

        // 获取数据
        String key = RedisConstant.DICT_PRE+"tree";
        List<TreeNode> tnlist = null;
        String tnStr = redisDao.get(key);
        if(!StrUtil.isEmpty(key)) {
            tnlist = JSON.parseArray(tnStr,TreeNode.class);
        }
        if (tnlist != null) {
            return tnlist;
        } else {
            String hql = "from Dict order by levelCode asc";
            List<Dict> dicts = this.find(hql);
            Map<String, TreeNode> nodelist = new LinkedHashMap<String, TreeNode>();
            for (Dict dict : dicts) {
                TreeNode node = new TreeNode();
                node.setText(dict.getName());
                node.setId(dict.getId());
                node.setParentId(dict.getParentId());
                node.setLevelCode(dict.getLevelCode());
                nodelist.put(node.getId(), node);
            }
            // 构造树形结构
            tnlist = TreeUtil.getNodeList(nodelist);
            redisDao.save(key, tnlist);
            return tnlist;
        }
    }

    public List<Dict> getDictsByCode(String code) {
        String key = RedisConstant.DICT_PRE+ code;
        List dicts = redisDao.get(key, List.class);
        if (dicts == null) {
            String hql = "from Dict where code='" + code + "'";
            Dict dict = this.get(hql);
            dicts = this.find("from Dict where parentId='" + dict.getId() + "' order by levelCode");
            redisDao.add(key, dicts);
            return dicts;
        } else {
            return dicts;
        }
    }


    public String getDictValueByCode(String code){
        String key=RedisConstant.DICT_PRE+code;
        String value=redisDao.get(key);
        if(StrUtil.isEmpty(value)){
            String hql = "from Dict where code='" + code + "'";
            Dict dict = this.get(hql);
            redisDao.add(key,dict.getValue());
            return dict.getValue();
        }else{
            return value;
        }
    }

    @Override
    public Dict getDictByCode(String code) {
        String hql = "from Dict where code='" + code + "'";
        Dict dict = this.get(hql);
        return dict;
    }

    @Override
    public Result checkExist(String code) {
        Dict dict = this.getDictByCode(code);
        if(dict==null)
            return new Result(false,"该字典列表尚未创建，请先创建");
        List<Dict> dicts = this.find("from Dict where parentId='" + dict.getId() + "' order by levelCode");
        if(dicts.isEmpty()){
           return new Result(false,"该字典编码下尚未创建子字典");
        }
        else{
            List<String> names=new ArrayList<>();
            dicts.forEach(d->{
                names.add(d.getName());
            });
            Map<String,String> params=new HashMap<>();
            params.put("names", StringUtils.join(names,","));
            params.put("parentName",dict.getName());
            return new Result(true,params,"成功");
        }
    }

    @Override
    public Result saveDictNames(String parentName, String parentCode, String names) {
        Dict parent=this.getDictByCode(parentCode);
        String parentId=null;
        if(parent==null){
             parent=new Dict();
             parent.setLevelCode(getMaxLevelCode());
             parent.setCode(parentCode);
             parent.setName(parentName);
            parent.setRemark("报表关联字典自动生成");
             parentId=super.save(parent).toString();
        }else{
            parent.setName(parentName);
            super.update(parent);
            parentId=parent.getId();
        }
        String[] nameArr=names.split(",");
        String parentLevelCode=parent.getLevelCode();
        if(nameArr.length<2){
            return new Result(false,"保存失败，请确认至少包含两个元素");
        }else{
            //清掉原来的子元素，重新添加
            // TODO 删除之前有个关联度校验 ReportColumnData
            super.executeHql("delete from Dict where parentId='"+parentId+"'");
            List<Dict> dicts=new ArrayList<>();
            for (String name : nameArr) {
                Dict dict=new Dict();
                dict.setName(name);
                dict.setParentId(parentId);
                dict.setCode(parentCode+"_"+ PingYinUtil.getFirstSpell(name).toUpperCase());
                dict.setLevelCode(codeGeneratorService.getLevelCode(Dict.class.getName(),parentId));
                dict.setRemark("报表关联字典自动生成");
                dicts.add(dict);
            }
            super.batchSave(dicts);
            this.deleteCacheByKey(RedisConstant.DICT_PRE + parentCode);
            this.deleteCacheByKey(RedisConstant.DICT_PRE + "tree");
        }
        return new Result();
    }

    public String getMaxLevelCode(){
        String sql="select max(levelCode) from tbl_dict where LENGTH(levelCode)=6";
        Object obj=this.getBySql(sql);
        return CodeUtil.nextCode("",obj.toString(),6);
    }

}
