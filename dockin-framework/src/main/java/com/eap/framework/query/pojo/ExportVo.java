package com.eap.framework.query.pojo;

import java.util.List;

/**
 * @author billjiang 475572229@qq.com
 * @create 18-1-31
 */
public class ExportVo {
    private String sheetName;
    private String sheetTitle;
    private List dataList;


    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public String getSheetTitle() {
        return sheetTitle;
    }

    public void setSheetTitle(String sheetTitle) {
        this.sheetTitle = sheetTitle;
    }

    public List getDataList() {
        return dataList;
    }

    public void setDataList(List dataList) {
        this.dataList = dataList;
    }
}
