package com.eap.report.entity;

import com.eap.framework.annotation.Header;
import com.eap.framework.base.entity.BaseEntity;

import javax.persistence.*;

/**
 * 列数据值
 * @author billjiang 475572229@qq.com
 * @create 18-9-7
 */
@Table(name="rpt_column_data")
@Entity
public class ReportColumnData extends BaseEntity {
    @Header(name="列")
    @Column(name="column_id")
    private String columnId;

    @Header(name = "填报机构")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_org")
    private ReportOrg reportOrg;

    //关联的ReportData
    @Header(name="行数据")
    @Column(name="data_id")
    private String dataId;

    @Header(name="值")
    @Column(name="[value]")
    private String value;

    public ReportOrg getReportOrg() {
        return reportOrg;
    }

    public void setReportOrg(ReportOrg reportOrg) {
        this.reportOrg = reportOrg;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getColumnId() {
        return columnId;
    }

    public void setColumnId(String columnId) {
        this.columnId = columnId;
    }
}
