package com.eap.report.service.impl;

import com.eap.framework.base.entity.Dict;
import com.eap.framework.base.entity.Org;
import com.eap.framework.base.pojo.Result;
import com.eap.framework.base.service.impl.BaseServiceImpl;
import com.eap.framework.utils.DateUtil;
import com.eap.framework.utils.StrUtil;
import com.eap.report.constant.ReportState;
import com.eap.report.entity.ReportEntity;
import com.eap.report.entity.ReportInstance;
import com.eap.report.entity.ReportOrg;
import com.eap.report.pojo.StrConstant;
import com.eap.report.service.ReportColumnService;
import com.eap.report.service.ReportInstanceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ReportInstanceServiceImpl extends BaseServiceImpl implements ReportInstanceService {

    @Resource
    private ReportColumnService reportColumnService;

    @Override
    public Result generateInstance(String reportId, String orgIds, String startDate, String endDate, Integer state) {
        // check column exist
        Result result = reportColumnService.checkColumnExist(reportId);
        if (!result.isSuccess())
            return result;
        // save the report
        ReportEntity report = this.get(ReportEntity.class, reportId);
        report.setState(ReportState.REPORT_STATE_SUBMIT.getValue());
        if (StrUtil.isNotBlank(startDate))
            report.setStartDate(DateUtil.parseToDate(startDate));
        if (StrUtil.isNotBlank(endDate))
            report.setEndDate(DateUtil.parseToDate(endDate));
        // generate the instance, check the instance exist or not
        // TODO 改成自动任务调度
        String[] names = this.getInstanceName(report.getExpression(), new Date(), report.getType());
        String instanceName = names[0];
        String dataLabel = names[1];
        ReportInstance instance = this.get("from ReportInstance where name='" + instanceName + "'");
        if (instance == null) {
            instance = new ReportInstance();
            instance.setName(instanceName);
            instance.setDataLabel(dataLabel);
            instance.setReportId(reportId);
            this.save(instance);
        }
        // before save the reportOrg delete the records first
        this.executeHql("delete ReportOrg where reportInstanceId='" + instance.getId() + "'");
        String[] orgIdArr = orgIds.split(",");
        List<ReportOrg> orgList = new ArrayList<>();
        for (String orgStr : orgIdArr) {
            ReportOrg reportOrg = new ReportOrg();
            reportOrg.setReportInstanceId(instance.getId());
            reportOrg.setReportInstanceName(instance.getName());
            reportOrg.setState(state);
            reportOrg.setOrgId(orgStr);
            Org org = this.get(Org.class, orgStr);
            reportOrg.setOrgName(org.getName());
            orgList.add(reportOrg);
        }
        this.batchSave(orgList);
        return new Result(true);
    }

    private String[] getInstanceName(String expression, Date date, Dict type) {
        String[] names = new String[2];
        String placehoder = "";
        date = date == null ? new Date() : date;
        if (!StrUtil.isEmpty(type.getRemark())) {
            placehoder = DateUtil.format(date, type.getRemark());
        }
        if (type.getCode().equals(StrConstant.REPORT_TYPE_SEASON.getValue())) {
            placehoder += "第" + DateUtil.getQuarter(date) + "季度";
        }
        if (type.getCode().equals(StrConstant.REPORT_TYPE_WEEK.getValue())) {
            placehoder += "第" + DateUtil.getWeekOfMonth(date) + "周";
        }
        List<String> codes = StrUtil.parseStrByTag(expression, "${", "}");
        for (String code : codes) {
            expression = expression.replace("${" + code + "}", placehoder);
        }
        names[0] = expression;
        names[1] = placehoder;
        return names;
    }

    @Override
    public String getOrgIds(String reportId) {
        ReportInstance instance = this.get("from ReportInstance where reportId='" + reportId + "' order by createDateTime desc");
        if(instance==null)
            return null;
        List<ReportOrg> orgs=this.find("from ReportOrg where reportInstanceId='"+instance.getId()+"'");
        if(orgs.isEmpty())
            return null;
        int size=orgs.size();
        String[] orgIdArr=new String[size];
        for (int i = 0; i < size; i++) {
            orgIdArr[i]=orgs.get(i).getOrgId();
        }
        return StrUtil.join(orgIdArr);
    }
}