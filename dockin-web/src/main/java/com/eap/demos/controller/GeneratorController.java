package com.eap.demos.controller;

import com.alibaba.fastjson.JSON;
import com.eap.demos.entity.Generator;
import com.eap.framework.annotation.RefreshCSRFToken;
import com.eap.framework.annotation.VerifyCSRFToken;
import com.eap.framework.base.entity.Dict;
import com.eap.framework.base.pojo.Result;
import com.eap.framework.base.service.BaseService;
import com.eap.framework.utils.StrUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 测试管理控制器
 *
 * @author jrn
 * 2017-02-20 14:15:46由代码生成器自动生成
 */
@Controller
@RequestMapping("/generator")
public class GeneratorController {

    @Resource
    private BaseService baseService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list() {
        return "demos/generator_list";
    }

    //可编辑表格 列编辑
    @RequestMapping(value = "/table/coledit", method = RequestMethod.GET)
    public String tableColEdit() {
        return "demos/generator_table_coledit";
    }


    //可编辑表格 列编辑
    @RequestMapping(value = "/table/rowedit", method = RequestMethod.GET)
    public String tableRowEdit() {
        return "demos/generator_table_rowedit";
    }

    @RefreshCSRFToken
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(String id, HttpServletRequest request) {
        request.setAttribute("id", id);
        return "demos/generator_edit";
    }

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public String detail(String id, HttpServletRequest request) {
        request.setAttribute("id", id);
        return "demos/generator_detail";
    }

    @RequestMapping(value = "/get/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Generator get(@PathVariable("id") String id) {
        return baseService.get(Generator.class, id);
    }

    @VerifyCSRFToken
    @RequestMapping(value = "/save")
    @ResponseBody
    public Result save(String obj) {
        Generator generator = JSON.parseObject(obj, Generator.class);
        generator.setNation(baseService.get(Dict.class, generator.getNation().getId()));
        if (StrUtil.isEmpty(generator.getId())) {
            baseService.save(generator);
        } else {
            Generator generator_old = baseService.get(Generator.class, generator.getId());
            BeanUtils.copyProperties(generator, generator_old,"email","remark");
            generator_old.setUpdateDateTime(new Date());
            baseService.update(generator_old);
        }
        return new Result(true);
    }

    @RequestMapping(value = "/saveObj")
    @ResponseBody
    public Result saveObj(String obj) {
        Generator generator = JSON.parseObject(obj, Generator.class);
        generator.setNation(baseService.get(Dict.class, generator.getNation().getId()));
        Generator generator_old = baseService.get(Generator.class, generator.getId());
        generator_old.setName(generator.getName());
        generator_old.setBirthday(generator.getBirthday());
        generator_old.setCode(generator.getCode());
        generator_old.setGender(generator.getGender());
        generator_old.setSalary(generator.getSalary());
        generator_old.setNation(generator.getNation());
        generator_old.setUpdateDateTime(new Date());
        baseService.update(generator_old);
        return new Result(true);
    }


    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Result delete(@PathVariable("id") String id) {
        Generator generator = this.get(id);
        try {
            baseService.delete(generator);
            return new Result();
        } catch (Exception e) {
            return new Result(false, "该数据已经被引用，不可删除");
        }
    }

    //批量删除
    @RequestMapping(value = "/deleteBatch/{ids}", method = RequestMethod.POST)
    @ResponseBody
    public Result deleteBatch(@PathVariable("ids") String ids) {
        String hql = "delete from Generator where id in (:ids)";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ids", ids.split(","));
        try {
            baseService.executeHql(hql, params);
            return new Result();
        } catch (Exception e) {
            return new Result(false, "该数据已经被引用，不可删除");
        }
    }


}
