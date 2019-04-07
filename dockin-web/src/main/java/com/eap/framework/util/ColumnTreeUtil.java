package com.eap.framework.util;

import com.eap.framework.utils.StrUtil;
import com.eap.report.pojo.ColumnTreeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author billjiang 475572229@qq.com
 * @create 19-4-2
 */
public class ColumnTreeUtil {

    public static List<ColumnTreeNode> getColumnNodeList(Map<String, ColumnTreeNode> nodelist) {
        List<ColumnTreeNode> tnlist=new ArrayList<>();
        for (String id : nodelist.keySet()) {
            ColumnTreeNode node = nodelist.get(id);
            if (StrUtil.isEmpty(node.getParentId())) {
                tnlist.add(node);
            } else {
                if (nodelist.get(node.getParentId()).getNodes() == null)
                    nodelist.get(node.getParentId()).setNodes(new ArrayList<ColumnTreeNode>());
                nodelist.get(node.getParentId()).getNodes().add(node);
            }
        }
        return tnlist;
    }

}
