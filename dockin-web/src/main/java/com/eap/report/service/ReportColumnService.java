package com.eap.report.service;

import com.eap.framework.base.pojo.Result;
import com.eap.framework.base.pojo.TreeNode;
import com.eap.framework.base.service.BaseService;
import com.eap.report.entity.ReportColumn;
import com.eap.report.pojo.ColumnTreeNode;

import java.util.List;

/**
 * @author billjiang 475572229@qq.com
 * @create 18-9-11
 */
public interface ReportColumnService extends BaseService {

    /**
     * 获取报表列的树结构
     * @param reportId
     * @return
     */
    List<TreeNode> getTreeData(String reportId);

    List<ColumnTreeNode> getColumnTreeData(String reportId);


    /**
     * 根据code获取列
     * @param reportId
     * @param code
     * @return
     */
    ReportColumn getColumnByCode(String reportId, String code);

    /**
     *
     * @param reportId
     * @param expression
     * @return
     */
    Result checkExpression(String reportId, String expression);

    /**
     * 校验是否可以提交
     * @param reportId
     * @return
     */
    Result checkColumnExist(String reportId);
}
