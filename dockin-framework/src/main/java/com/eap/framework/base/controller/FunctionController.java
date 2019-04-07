package com.eap.framework.base.controller;

import com.eap.framework.base.entity.Function;
import com.eap.framework.base.pojo.Result;
import com.eap.framework.base.pojo.TreeNode;
import com.eap.framework.base.service.FunctionService;
import com.eap.framework.utils.StrUtil;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.*;

@Controller
@RequestMapping(value = "/function")
public class FunctionController {

    @Resource
    private FunctionService functionService;

    /**
     * 用户列表
     */
    @RequestMapping(method = RequestMethod.GET, value = "/tree")
    private String list() {

        return "base/auth/function_tree";
    }

    @RequestMapping(value = "/all", method = RequestMethod.POST)
    @ResponseBody
    public List<Function> getAll() {

        String hql = "from Function order by levelCode asc";
        return functionService.find(hql.toString());
    }

    @RequestMapping(value = "/list/{roleId}", method = RequestMethod.POST)
    @ResponseBody
    public List<String> getFunctionIdsByRoleItd(@PathVariable("roleId") String roleId) {
        String hql = "select functionId from RoleFunction where roleId=:roleId";
        Map<String, Object> param = new HashMap<>();
        param.put("roleId", roleId);
        return functionService.find(hql, param);
    }

    /**
     * getTreeData 构造bootstrap-treeview格式数据
     *
     * @return
     */
    @RequestMapping(value = "/treeData", method = RequestMethod.POST)
    @ResponseBody
    public List<TreeNode> getTreeData() {

        return functionService.getTreeData();
    }

    @RequestMapping(value = "/listTree/{roleId}", method = RequestMethod.POST)
    @ResponseBody
    public List<TreeNode> getTreeDataByRoleId(@PathVariable("roleId") String roleId) {
        return functionService.getTreeDataByRoleId(roleId);
    }

    @RequestMapping(value = "/get/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Function get(@PathVariable("id") String id) {

        Function function = functionService.get(Function.class, id);
        if (!StrUtil.isEmpty(function.getParentId())) {
            function.setParentName(functionService.get(Function.class, function.getParentId()).getName());
        } else {
            function.setParentName("系统菜单");
        }
        return function;
    }

    // 获取面包屑导航
    @RequestMapping(value = "/getFunctions/{code}", method = RequestMethod.POST)
    @ResponseBody
    public List<Function> getBreadcrumb(@PathVariable("code") String code) {
        List<Function> functions = new ArrayList<>();
        Function function = functionService.get("from Function where code='" + code + "'");
        if(function!=null) {
            String levelCode = function.getLevelCode();
            int len = levelCode.length() / 6;
            String[] levelCodes = new String[len];
            for (int i = 0; i < len; i++) {
                levelCodes[i] = levelCode.substring(0, (i + 1) * 6);
            }
            DetachedCriteria criteria = DetachedCriteria.forClass(Function.class);
            criteria.add(Restrictions.in("levelCode",levelCodes));
            criteria.addOrder(Order.asc("levelCode"));
            functions=functionService.findByCriteria(criteria);
        }
        return functions;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public Result save(Function function) {
        function.setUpdateDateTime(new Date());
        functionService.saveOrUpdate(function);
        return new Result(true);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Result delete(@PathVariable("id") String id) {

        try {
            Function function = functionService.get(Function.class, id);
            functionService.delete(function);
            return new Result(true);
        } catch (Exception e) {
            return new Result(false, "该菜单/功能已经被其他数据引用，不可删除");
        }
    }


    //TODO 功能集合将从session中获取
    @RequestMapping(value = "/navigation")
    @ResponseBody
    public List<Function> navigation(String pageUrl) {
        return functionService.getAll();
    }


}
