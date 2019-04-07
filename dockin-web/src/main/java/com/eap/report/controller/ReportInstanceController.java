package com.eap.report.controller;

import com.eap.framework.base.pojo.Result;
import com.eap.framework.base.service.BaseService;
import com.eap.report.entity.ReportInstance;
import com.eap.report.service.ReportInstanceService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 报表实例
 *
 * @author billjiang 475572229@qq.com
 * @create 19-4-3
 */
@Controller
@RequestMapping(value = "/report/instance")
public class ReportInstanceController {

    @Resource
    private ReportInstanceService reportInstanceService;

    /**
     * 报表实例生成
     */
    @RequestMapping(value = "/generate", method = RequestMethod.POST)
    @ResponseBody
    public Result generateInstance(String reportId, String orgIds, String startDate, String endDate,String state) {
        return reportInstanceService.generateInstance(reportId, orgIds, startDate, endDate,Integer.valueOf(state));
    }


}