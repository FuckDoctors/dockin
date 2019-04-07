package com.eap.report.entity;

import com.eap.framework.annotation.Header;
import com.eap.framework.base.entity.BaseEntity;
import com.eap.framework.base.entity.Dict;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

/**
 * 报表数据
 *
 * @author billjiang 475572229@qq.com
 * @create 18-9-6
 */
@Table(name = "rpt_data")
@Entity
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
public class ReportData extends BaseEntity{

    @Header(name="值")
    @Column(name="data",columnDefinition = "TEXT")
    private String data;

    @Header(name = "填报机构")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_org")
    private ReportOrg reportOrg;


    @Header(name="状态")
    @Column(name = "state")
    private Integer state;

    public ReportOrg getReportOrg() {
        return reportOrg;
    }

    public void setReportOrg(ReportOrg reportOrg) {
        this.reportOrg = reportOrg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
