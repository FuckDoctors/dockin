package com.eap.report.entity;

import com.eap.framework.annotation.Header;
import com.eap.framework.base.entity.BaseEntity;
import com.eap.framework.base.entity.Dict;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

/**
 * 填报数据列定义
 *
 * @author billjiang 475572229@qq.com
 * @create 18-9-6
 */
@Entity
@Table(name="rpt_column")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
public class ReportColumn extends BaseEntity {

    @Header(name = "名称")
    @Column(name = "name")
    private String name;

    @Header(name = "编码")
    @Column(name = "[code]")
    private String code;

    @Header(name="父列")
    @Column(name="parent_id")
    private String parentId;

    @Header(name="层级编码")
    @Column(name="level_code")
    private String levelCode;


    @Header(name = "数据类型")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "data_type")
    private Dict dataType;

    @Header(name = "控件类型")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "component_type")
    private Dict componentType;

    @Header(name="保留位数")
    @Column(name="[precision]")
    private Integer precision;

    @Header(name="是否必填")
    @Column(name="not_null")
    private String notNull;

    @Header(name="默认值")
    @Column(name="default_value")
    private String default_value;

    // 数据列类型 统计列 一般列 父列
    @Header(name="列类型")
    @Column(name="column_type")
    private String columnType;

    @Header(name="计算表达式")
    @Column(name="expression")
    private String expression;

    @Header(name="字典编码")
    @Column(name="dict_code")
    private String dictCode;

    @Header(name = "报表")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id")
    private ReportEntity report;

    @Transient
    private String reportName;

    public String getLevelCode() {
        return levelCode;
    }

    public void setLevelCode(String levelCode) {
        this.levelCode = levelCode;
    }

    public String getParentId() {
        return parentId;
    }

    public Integer getPrecision() {
        return precision;
    }

    public void setPrecision(Integer precision) {
        this.precision = precision;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Dict getComponentType() {
        return componentType;
    }

    public void setComponentType(Dict componentType) {
        this.componentType = componentType;
    }

    public Dict getDataType() {
        return dataType;
    }

    public void setDataType(Dict dataType) {
        this.dataType = dataType;
    }


    public String getNotNull() {
        return notNull;
    }

    public void setNotNull(String notNull) {
        this.notNull = notNull;
    }

    public String getDefault_value() {
        return default_value;
    }

    public void setDefault_value(String default_value) {
        this.default_value = default_value;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getDictCode() {
        return dictCode;
    }

    public void setDictCode(String dictCode) {
        this.dictCode = dictCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ReportEntity getReport() {
        return report;
    }

    public void setReport(ReportEntity report) {
        this.report = report;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }
}
