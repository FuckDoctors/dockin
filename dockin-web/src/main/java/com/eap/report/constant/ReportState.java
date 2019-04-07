package com.eap.report.constant;

/**
 * @author billjiang 475572229@qq.com
 * @create 18-10-9
 */
public enum ReportState {

    REPORT_STATE_SAVE(0),

    REPORT_STATE_SUBMIT(10),

    REPORT_ORG_STATE_SAVE(0),

    REPORT_ORG_STATE_SUBMIT(10);

    private Integer value;

    ReportState(Integer value){
        this.value=value;
    }

    public Integer getValue(){
        return this.value;
    }

}
