package com.eap.report.service.impl;

import com.eap.framework.base.pojo.Result;
import com.eap.framework.base.pojo.TreeNode;
import com.eap.framework.base.service.impl.BaseServiceImpl;
import com.eap.framework.util.ColumnTreeUtil;
import com.eap.framework.utils.ExpressionUtil;
import com.eap.framework.utils.TreeUtil;
import com.eap.report.entity.ReportColumn;
import com.eap.report.pojo.ColumnTreeNode;
import com.eap.report.pojo.StrConstant;
import com.eap.report.service.ReportColumnService;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author billjiang 475572229@qq.com
 * @create 18-9-11
 */
@Service("reportColumnService")
public class ReportColumnServiceImpl extends BaseServiceImpl implements ReportColumnService {

    @Override
    public List<TreeNode> getTreeData(String reportId) {
        // 获取数据
        List<TreeNode> tnlist = null;
        DetachedCriteria criteria = DetachedCriteria.forClass(ReportColumn.class);
        criteria.add(Restrictions.eq("report.id", reportId));
        criteria.addOrder(Order.asc("levelCode"));
        List<ReportColumn> columns = super.findByCriteria(criteria);
        Map<String, TreeNode> nodelist = new LinkedHashMap<>();
        for (ReportColumn column : columns) {
            TreeNode node = new TreeNode();
            node.setText(column.getName());
            node.setId(column.getId());
            node.setTags(null);
            node.setParentId(column.getParentId());
            node.setLevelCode(column.getLevelCode());
            nodelist.put(node.getId(), node);
        }
        // 构造树形结构
        tnlist = TreeUtil.getNodeList(nodelist);
        return tnlist;
    }

    @Override
    public List<ColumnTreeNode> getColumnTreeData(String reportId) {
        // 获取数据
        List<ColumnTreeNode> tnlist = null;
        DetachedCriteria criteria = DetachedCriteria.forClass(ReportColumn.class);
        criteria.add(Restrictions.eq("report.id", reportId));
        criteria.addOrder(Order.asc("levelCode"));
        List<ReportColumn> columns = super.findByCriteria(criteria);
        Map<String, ColumnTreeNode> nodelist = new LinkedHashMap<>();
        for (ReportColumn column : columns) {
            ColumnTreeNode node = new ColumnTreeNode();
            node.setText(column.getName());
            node.setId(column.getId());
            node.setTags(null);
            node.setParentId(column.getParentId());
            node.setLevelCode(column.getLevelCode());
            node.setReportColumn(column);
            nodelist.put(node.getId(), node);
        }
        // 构造树形结构
        tnlist = (List<ColumnTreeNode>) ColumnTreeUtil.getColumnNodeList(nodelist);
        return tnlist;
    }


    @Override
    public ReportColumn getColumnByCode(String reportId, String code) {
        DetachedCriteria criteria = DetachedCriteria.forClass(ReportColumn.class);
        criteria.add(Restrictions.eq("report.id", reportId));
        criteria.add(Restrictions.eq("code", code));
        List<ReportColumn> columns = super.findByCriteria(criteria);
        if (columns.isEmpty())
            return null;
        else
            return columns.get(0);
    }

    @Override
    public Result checkExpression(String reportId, String expression) {
        List<ReportColumn> columns = this.getColumns(reportId);
        List<String> exps = ExpressionUtil.parseStrByTag(expression, "${", "}");
        for (String code : exps) {
            boolean exist = false;
            for (ReportColumn column : columns) {
                if (column.getCode().equals(code)) {
                    if (!column.getDataType().getCode().equals("Integer")&&!column.getDataType().getCode().equals("Double")) {
                        return new Result(false, "编码为【" + code + "】的列的数据类型不是整数也不是小数");
                    } else {
                        exist = true;
                        break;
                    }
                }
            }
            if (!exist) {
                return new Result(false, "编码为【" + code + "】的列不存在");
            }
        }
        return new Result(true);
    }

    @Override
    public Result checkColumnExist(String reportId) {
        // 定义列
        List<ReportColumn> columns=this.getColumns(reportId);
        if(columns.isEmpty()){
            return new Result(false,"尚未定义列，不可提交");
        }
        //定义的列中至少有一个列是填报列
        boolean hasColumnForInput=false;
        for (ReportColumn column : columns) {
           if(column.getColumnType().equals(StrConstant.COLUMN_TYPE_NORMAL.getValue())){
               hasColumnForInput=true;
               break;
           }
        }
        if(!hasColumnForInput)
            return new Result(false,"至少定义一个填报列");
        return new Result();
    }


    public List<ReportColumn> getColumns(String reportId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(ReportColumn.class);
        criteria.add(Restrictions.eq("report.id", reportId));
        return super.findByCriteria(criteria);
    }
}
