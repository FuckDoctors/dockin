package com.eap.report.entity;

import com.eap.framework.annotation.Header;
import com.eap.framework.base.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 填报机构（显示哪些机构已经填报）
 *
 * @author billjiang 475572229@qq.com
 * @create 18-9-6
 */
@Table(name="rpt_org")
@Entity
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class ReportOrg extends BaseEntity {

    @Header(name="报表实例")
    @Column(name="report_instance_id",length = 36)
    private String reportInstanceId;

    //冗余字段
    @Header(name="报表名称")
    @Column(name="report_name")
    private String reportName;

    @Header(name="填报单位")
    @Column(name="org_id",length=36)
    private String orgId;

    //冗余字段
    @Header(name="填报单位名称")
    @Column(name="org_name")
    private String orgName;

    //冗余字段
    @Header(name="报表实例名称")
    @Column(name="report_instance_name")
    private String reportInstanceName;

    @Header(name="状态")
    @Column(name="state")
    private Integer state;

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getReportInstanceName() {
        return reportInstanceName;
    }

    public void setReportInstanceName(String reportInstanceName) {
        this.reportInstanceName = reportInstanceName;
    }

    public String getReportInstanceId() {
        return reportInstanceId;
    }

    public void setReportInstanceId(String reportInstanceId) {
        this.reportInstanceId = reportInstanceId;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
}
