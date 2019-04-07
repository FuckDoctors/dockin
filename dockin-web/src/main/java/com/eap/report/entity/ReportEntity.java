package com.eap.report.entity;

import com.eap.framework.annotation.Header;
import com.eap.framework.base.entity.BaseEntity;
import com.eap.framework.base.entity.Dict;
import com.eap.report.constant.ReportState;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;

/**
 * 报表实体-
 *
 * @author billjiang 475572229@qq.com
 * @create 18-9-5
 */
@Table(name = "rpt_report")
@Entity
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
public class ReportEntity extends BaseEntity {

    @Header(name = "名称")
    @Column(name = "name")
    private String name;

    // 根据报表类型生成实例报表名称，
    // 如果配置了定时任务，则定时生成报表示例，替换事件类型存储在type中
    @Header(name = "实例名称")
    @Column(name = "expression")
    private String expression;

    // 年报 月报 周报 日报 一次性
    @Header(name = "类型")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dict_type")
    private Dict type;

    @Header(name = "填写说明")
    @Column(name = "remark", length = 4000)
    private String remark;

    // 0=临时保存  10=提交
    @Header(name = "状态")
    @Column(name = "state")
    private Integer state;

    @Header(name = "排序")
    @Column(name = "sort")
    private Integer sort;

    @Header(name="开始时间")
    @Column(name="start_date")
    private Date startDate;

    @Header(name="结束时间")
    @Column(name="end_date")
    private Date endDate;

    @Transient
    private String orgIds;

    public String getOrgIds() {
        return orgIds;
    }



    public void setOrgIds(String orgIds) {
        this.orgIds = orgIds;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public Integer getState() {
        if (state == null)
            return ReportState.REPORT_STATE_SAVE.getValue();
        return state;
    }

    public void setState(Integer state) {
        if (state == null)
            this.state = ReportState.REPORT_STATE_SAVE.getValue();
        else
            this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Dict getType() {
        return type;
    }

    public void setType(Dict type) {
        this.type = type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
