package com.eap.framework.base.service;

import java.util.List;

import com.eap.framework.base.entity.Dict;
import com.eap.framework.base.pojo.Result;
import com.eap.framework.base.pojo.TreeNode;

public interface DictService extends BaseService {

    List<TreeNode> getTreeData();

    List<Dict> getDictsByCode(String code);

    String getDictValueByCode(String code);

    Dict getDictByCode(String code);

    Result checkExist(String code);

    Result saveDictNames(String parentName, String parentCode, String names);
}
