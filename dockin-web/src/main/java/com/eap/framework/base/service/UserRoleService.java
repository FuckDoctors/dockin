package com.eap.framework.base.service;

import com.eap.framework.base.pojo.Result;

public interface UserRoleService extends BaseService {

    Result delete(String ids);

    void deleteAuthInRedis(String userId);

    void setRoleForRegisterUser(String userId);



}
