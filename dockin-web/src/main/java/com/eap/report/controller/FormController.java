package com.eap.report.controller;

import com.eap.framework.base.service.BaseService;
import com.eap.report.pojo.ColumnTreeNode;
import com.eap.report.service.ReportColumnService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 报表填报控制器
 * @author billjiang 475572229@qq.com
 * @create 19-4-2
 */
@Controller
@RequestMapping(value = "/report")
public class FormController {

    @Resource
    private BaseService baseService;

    @Resource
    private ReportColumnService reportColumnService;

    // 报表填报
    @RequestMapping(value = "/form/edit/{reportId}",method = RequestMethod.GET)
    public String formEdit(@PathVariable("reportId") String reportId, HttpServletRequest request){
        request.setAttribute("reportId",reportId);
        // 根据reportId 获取列属性配置
        List<ColumnTreeNode> columnList=reportColumnService.getColumnTreeData(reportId);
        request.setAttribute("columns",columnList);
        return "report/form/form_edit";
    }

    // 报表填报列表
    @RequestMapping(value = "/form/list",method = RequestMethod.GET)
    public String formList(){
        // TODO 根据填报机构获取填报列表
       return "test";
    }




}
