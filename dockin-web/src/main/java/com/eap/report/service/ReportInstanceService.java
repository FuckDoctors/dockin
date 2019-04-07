package com.eap.report.service;

import com.eap.framework.base.pojo.Result;
import com.eap.framework.base.service.BaseService;
import com.eap.report.entity.ReportInstance;
import com.eap.report.pojo.StrConstant;

/**
 * 报表实例服务接口
 *
 * @author billjiang 475572229@qq.com
 * @create 19-4-3
 */
public interface ReportInstanceService extends BaseService {

    public Result generateInstance(String reportId, String orgIds, String startDate, String endDate,Integer state);

    String getOrgIds(String id);
}
