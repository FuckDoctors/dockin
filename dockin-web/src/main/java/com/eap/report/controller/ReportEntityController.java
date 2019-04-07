package com.eap.report.controller;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.eap.framework.base.entity.Dict;
import com.eap.framework.utils.StrUtil;
import com.eap.report.service.ReportColumnService;
import com.eap.report.service.ReportInstanceService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eap.framework.base.service.BaseService;
import com.eap.framework.annotation.RefreshCSRFToken;
import com.eap.framework.annotation.VerifyCSRFToken;
import com.eap.framework.base.pojo.Result;
import com.eap.report.entity.ReportEntity;

/**
* 报表管理控制器
* @author jrn
* 2018-09-10 13:53:49由代码生成器自动生成
*/
@Controller
@RequestMapping("/report")
public class ReportEntityController {

    @Resource
    private BaseService baseService;

    @Resource
    private ReportColumnService reportColumnService;

    @Resource
    private ReportInstanceService reportInstanceService;

    @RequestMapping(value="/list",method = RequestMethod.GET)
    public String list(){
        return "report/report_list";
    }

    @RefreshCSRFToken
    @RequestMapping(value="/edit",method = RequestMethod.GET)
    public String edit(String id,HttpServletRequest request){
        request.setAttribute("id", id);
        return "report/report_edit";
    }

    @RequestMapping(value="/detail",method = RequestMethod.GET)
    public String detail(String id,HttpServletRequest request){
        request.setAttribute("id", id);
        return "report/report_detail";
    }


    @RequestMapping(value="/get/{id}",method = RequestMethod.POST)
    @ResponseBody
    public ReportEntity get(@PathVariable("id") String id){
        ReportEntity report= baseService.get(ReportEntity.class, id);
        report.setOrgIds(reportInstanceService.getOrgIds(id));
        return report;
    }



    @RequestMapping(value="/getList",method = RequestMethod.POST)
    @ResponseBody
    public List<ReportEntity> getList(){
        return baseService.find("from ReportEntity");
    }


    @RequestMapping(value="/save")
    @ResponseBody
    public Result save(String obj){
        ReportEntity report= JSON.parseObject(obj,ReportEntity.class);
        report.setType(baseService.get(Dict.class,report.getType().getId()));
        if(StrUtil.isEmpty(report.getId())){
            baseService.save(report);
        }
        else{
            report.setUpdateDateTime(new Date());
            baseService.update(report);
        }
        return new Result(true);
    }



    @RequestMapping(value="/delete/{id}",method = RequestMethod.POST)
    @ResponseBody
    public Result delete(@PathVariable("id") String id){
        ReportEntity report=this.get(id);
        try{
            baseService.delete(report);
            return new Result();
        }
        catch(Exception e){
            return new Result(false,"该数据已经被引用，不可删除");
        }
    }

    // 检查报表能否提交
    @RequestMapping(value ="/checkSubmit/{id}",method = RequestMethod.POST)
    @ResponseBody
    public Result checkCanSubmit(@PathVariable("id") String id){
        return reportColumnService.checkColumnExist(id);
    }


    //选择
    @RequestMapping(value = "/orgSelector/{id}",method = RequestMethod.GET)
    public String orgSelector(@PathVariable("id") String id,HttpServletRequest request){
        request.setAttribute("id",id);
        return "report/report_orgSelector";
    }


}
