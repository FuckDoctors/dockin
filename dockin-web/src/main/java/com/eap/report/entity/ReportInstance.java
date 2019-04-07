package com.eap.report.entity;

import com.eap.framework.annotation.Header;
import com.eap.framework.base.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * 报表实例
 * @author billjiang 475572229@qq.com
 * @create 18-9-6
 */
@Table(name="rpt_instance")
@Entity
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
public class ReportInstance extends BaseEntity{

    @Header(name="名称")
    @Column(name="name")
    private String name;

    @Header(name="关联报表")
    @Column(name="report_id",length = 36)
    private String reportId;

    //和月报、日报、年报时间格式相似
    @Header(name="报表时间")
    @Column(name="date_label")
    private String dataLabel;



    public String getDataLabel() {
        return dataLabel;
    }

    public void setDataLabel(String dataLabel) {
        this.dataLabel = dataLabel;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }
}
