package com.eap.framework.util;

import com.eap.framework.base.entity.User;
import org.apache.shiro.SecurityUtils;

/**
 * Created by billJiang on 2017/2/6.
 * e-mail:jrn1012@petrochina.com.cn qq:475572229
 */
public class SecurityUtil {

    public static String getUserId() {
        if (SecurityUtils.getSubject().getPrincipal() != null)
            return SecurityUtils.getSubject().getPrincipal().toString();
        else
            return null;
    }

    public static User getUser() {
        return (User) SecurityUtils.getSubject().getSession().getAttribute("user");
    }
}
