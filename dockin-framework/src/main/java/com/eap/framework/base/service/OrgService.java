package com.eap.framework.base.service;

import com.eap.framework.base.entity.Org;
import com.eap.framework.base.pojo.Result;
import com.eap.framework.base.pojo.TreeNode;

import java.util.List;

public interface OrgService extends BaseService {

    List<TreeNode> getTreeData();

    List<Org> getOrgsByCode(String code);

    boolean referByUser(String orgId);

    Result getOrgNames(String id);
}
