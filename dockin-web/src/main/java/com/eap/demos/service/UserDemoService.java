package com.eap.demos.service;

import com.eap.framework.base.entity.User;
import com.eap.framework.base.service.BaseService;

/**
 * Created by HANZO on 2016/6/17.
 */
public interface UserDemoService extends BaseService {

    User saveUser(User user);

}
