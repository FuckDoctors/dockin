package com.eap.report.pojo;

import com.eap.report.entity.ReportColumn;

import java.util.List;

/**
 * @author billjiang 475572229@qq.com
 * @create 19-4-2
 */
public class ColumnTreeNode{

    private String text;

    private List<String> tags;

    private String id;

    private String parentId;

    private String levelCode;

    private List<ColumnTreeNode> nodes;

    private String icon;

    private ReportColumn reportColumn;

    public ReportColumn getReportColumn() {
        return reportColumn;
    }

    public void setReportColumn(ReportColumn reportColumn) {
        this.reportColumn = reportColumn;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getLevelCode() {
        return levelCode;
    }

    public void setLevelCode(String levelCode) {
        this.levelCode = levelCode;
    }

    public List<ColumnTreeNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<ColumnTreeNode> nodes) {
        this.nodes = nodes;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
