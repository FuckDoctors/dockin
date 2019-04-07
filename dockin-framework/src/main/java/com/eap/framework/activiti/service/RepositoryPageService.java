package com.eap.framework.activiti.service;

import com.eap.framework.activiti.pojo.ProcessDefVo;
import com.eap.framework.base.pojo.PageInfo;
import com.eap.framework.base.service.BaseService;
import com.eap.framework.query.entity.QueryCondition;
import org.activiti.engine.repository.Model;

import java.util.List;

/**
 * Created by billJiang on 2017/6/8.
 * e-mail:475572229@qq.com  qq:475572229
 * 流程定义服务接口
 */
public interface RepositoryPageService extends BaseService {
    List<ProcessDefVo> getProcessDefList(QueryCondition condition, PageInfo pageInfo);

    /**
     * 根据分页获取模型列表
     * @param condition
     * @param pageInfo
     * @return
     */
    List<Model> getModelList(QueryCondition condition, PageInfo pageInfo);
}
