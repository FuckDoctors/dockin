package com.eap.report.pojo;

/**
 * @author billjiang 475572229@qq.com
 * @create 18-9-6
 */
public enum StrConstant {

    COLUMN_TYPE_NORMAL("normal"),
    COLUMN_TYPE_CALC("calc"),
    COLUMN_TYPE_PARENT("parent"),
    YES("yes"),
    NO("no"),

    //报表类型
    REPORT_TYPE_YEAR("RPT_TYPE_YEAR"),
    REPORT_TYPE_SEASON("RPT_TYPE_SEASON"),
    REPORT_TYPE_MONTH("RPT_TYPE_MONTH"),
    REPORT_TYPE_WEEK("RPT_TYPE_WEEK"),
    REPORT_TYPE_DAY("RPT_TYPE_DAY"),
    REPORT_TYPE_ONCE("RPT_TYPE_ONCE");

    private String value;

    StrConstant(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
