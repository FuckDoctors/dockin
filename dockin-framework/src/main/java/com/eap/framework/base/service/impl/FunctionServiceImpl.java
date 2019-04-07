package com.eap.framework.base.service.impl;

import com.eap.framework.base.dao.RedisDao;
import com.eap.framework.base.entity.Function;
import com.eap.framework.base.entity.FunctionFilter;
import com.eap.framework.base.entity.RoleFunction;
import com.eap.framework.base.pojo.TreeNode;
import com.eap.framework.base.service.FunctionService;
import com.eap.framework.constant.RedisConstant;
import com.eap.framework.utils.TreeUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service("functionService")
public class FunctionServiceImpl extends BaseServiceImpl implements FunctionService {

    @Resource
    private RedisDao redisDao;


    @Override
    public List<TreeNode> getTreeData() {

        // 获取数据
        String hql = "from Function order by levelCode asc";
        List<Function> funcs = this.find(hql);
        return getTreeNodes(funcs);
    }

    public List<TreeNode> getTreeNodes(List<Function> funcs) {
        Map<String, TreeNode> nodelist = new LinkedHashMap<String, TreeNode>();
        for (Function func : funcs) {
            TreeNode node = new TreeNode();
            node.setText(func.getName());
            node.setId(func.getId());
            node.setParentId(func.getParentId());
            node.setLevelCode(func.getLevelCode());
            node.setIcon(func.getIcon());
            nodelist.put(node.getId(), node);
        }
        // 构造树形结构
        return TreeUtil.getNodeList(nodelist);

    }

    @Override
    public List<TreeNode> getTreeDataByRoleId(String roleId) {
        String sql = "select f.* from tbl_role_function rf " +
                "left join tbl_function f on rf.functionId=f.id " +
                "left join tbl_role r on rf.roleId=r.id " +
                "where r.id=:roleId";
        //String[] strs = (String[])roleCodes.toArray();
        Map<String, Object> params = new HashMap<>();
        params.put("roleId", roleId);
        List<Function> funcs = super.findBySql(sql, params, Function.class);
        return getTreeNodes(funcs);
    }

    @Override
    public List<Function> getAll() {
        String hql = "from Function where (deleted=0 or deleted is null) order by levelCode";
        return this.find(hql);
    }

    @Override
    public Set<String> getFunctionCodeSet(Set<String> roleCodes, String userId) {
        if (roleCodes.size() == 0)
            return null;
        List<Function> functions = getFunctionList(roleCodes, userId);
        Set<String> retSet = new HashSet<>();
        for (Function function : functions) {
            retSet.add(function.getCode());
        }
        return retSet;
    }

    @Override
    public Set<String> getAllFunctionCode() {
        Set<String> sets = new HashSet<>();
        List<Function> functions = this.getAll();
        for (Function function : functions) {
            sets.add(function.getCode());
        }
        return sets;
    }


    @Override
    public List<Function> getFunctionList(Set<String> roleCodes, String userId) {
        String sql = "select rf.* from tbl_role_function rf left join tbl_role r on rf.roleId=r.id" +
                " left join tbl_function f on rf.functionId=f.id  where r.code in (:roleCodes) and (f.deleted=0 or f.deleted is null)";
        //String[] strs = (String[])roleCodes.toArray();
        Map<String, Object> params = new HashMap<>();
        params.put("roleCodes", roleCodes);
        List<RoleFunction> roleFunctionList = super.findBySql(sql, params, RoleFunction.class);
        List<Function> functionList = this.getFunctionListWithoutRepeat(roleFunctionList);

        return functionList;

    }

    //functionList去重，并注入数据权限FunctionFilter
    public List<Function> getFunctionListWithoutRepeat(List<RoleFunction> roleFunctions) {
        List<Function> list = new ArrayList<Function>();
        Map<String, Boolean> map = new HashMap<>();
        for (RoleFunction roleFunction : roleFunctions) {
            if (map.containsKey(roleFunction.getFunctionId())) {
                Function function = getFunctionById(roleFunction.getFunctionId(), list);
                List<FunctionFilter> fflist = getFunctionFilters(roleFunction.getRoleId(), roleFunction.getFunctionId());
                // 数据权限合并，以大数据范围优先，
                // TODO 数据权限选择逻辑还需要进一步优化，可设置优先级
                if (fflist.size() < function.getFflist().size()) {
                    function.setFflist(fflist);
                }
            } else {
                map.put(roleFunction.getFunctionId(), true);
                Function function = this.get(Function.class, roleFunction.getFunctionId());
                //获取数据权限
                List<FunctionFilter> fflist = getFunctionFilters(roleFunction.getRoleId(), roleFunction.getFunctionId());
                function.setFflist(fflist);
                list.add(function);
            }

        }
        return list;
    }

    public Function getFunctionById(String id, List<Function> functions) {
        for (Function function : functions) {
            if (function.getId().equals(id)) {
                return function;
            }
        }
        return null;
    }

    public List<FunctionFilter> getFunctionFilters(String roleId, String functionId) {
        String hql = "from FunctionFilter where roleId=:roleId and functionId=:functionId order by sort";
        Map<String, Object> param = new HashMap<>();
        param.put("roleId", roleId);
        param.put("functionId", functionId);
        return this.find(hql, param);
    }
}
