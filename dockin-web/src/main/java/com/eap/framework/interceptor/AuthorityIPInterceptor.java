package com.eap.framework.interceptor;

import com.eap.framework.base.entity.Dict;
import com.eap.framework.base.service.DictService;
import com.eap.framework.utils.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * IP拦截器
 *
 * @author billjiang 475572229@qq.com
 * @create 18-6-9
 */
public class AuthorityIPInterceptor extends HandlerInterceptorAdapter {
    private final static Logger logger = LoggerFactory.getLogger(AuthorityIPInterceptor.class);


    @Resource
    private DictService dictService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (handler instanceof HandlerMethod) {
            /*IPFilter ipFilter = ((HandlerMethod) handler).getMethodAnnotation(IPFilter.class);
            if (ipFilter == null) {
                return true;
            }
            String ipAddress = getIpAddress(request);
            ArrayList<String> deny = Lists.newArrayList(ipFilter.deny());
            ArrayList<String> allow = Lists.newArrayList(ipFilter.allow());

            //设置了黑名单,  黑名单内ip不给通过
            if (CollectionUtils.isNotEmpty(deny)) {
                if (deny.contains(ipAddress)) {
                    logger.info("IP: "+ipAddress+" 被拦截");
                    response.setStatus(500);
                    return false;
                }
            }*/
            String ipAddress = getIpAddress(request);
            Dict allowDict = dictService.getDictByCode("ip-allowed");
            if(allowDict==null)
                return true;
            String allows = allowDict.getValue();
            //设置了白名单,  只有白名单内的ip给通过 ,没有ip的时候 自动增加ip
            if (!StrUtil.isEmpty(allows)) {
                List<String> allow = Arrays.asList(allows.split(","));
                if (allow.contains(ipAddress)) {
                    logger.warn("IP: " + ipAddress + " 被放行");
                    return true;
                } else {
                    if(allowDict.getDeleted()==1) {
                       allowDict.setValue(allows+","+ipAddress);
                       dictService.update(allowDict);
                    }else{
                        logger.error("IP: " + ipAddress + " 没有放行权利");
                        response.setStatus(500);
                        return false;
                    }
                }
            }else if(allowDict.getDeleted()==1){
                allowDict.setValue(ipAddress);
                dictService.update(allowDict);
            }
        }
        return true;
    }

    /**
     * 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址;
     *
     * @param request
     * @return
     * @throws IOException
     */
    public final static String getIpAddress(HttpServletRequest request) throws IOException {
        // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址

        String ip = request.getHeader("X-Forwarded-For");
        if (logger.isInfoEnabled()) {
            logger.info("getIpAddress(HttpServletRequest) - X-Forwarded-For - String ip=" + ip);
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
                if (logger.isInfoEnabled()) {
                    logger.info("getIpAddress(HttpServletRequest) - Proxy-Client-IP - String ip=" + ip);
                }
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
                if (logger.isInfoEnabled()) {
                    logger.info("getIpAddress(HttpServletRequest) - WL-Proxy-Client-IP - String ip=" + ip);
                }
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
                if (logger.isInfoEnabled()) {
                    logger.info("getIpAddress(HttpServletRequest) - HTTP_CLIENT_IP - String ip=" + ip);
                }
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
                if (logger.isInfoEnabled()) {
                    logger.info("getIpAddress(HttpServletRequest) - HTTP_X_FORWARDED_FOR - String ip=" + ip);
                }
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
                if (logger.isInfoEnabled()) {
                    logger.info("getIpAddress(HttpServletRequest) - getRemoteAddr - String ip=" + ip);
                }
            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (int index = 0; index < ips.length; index++) {
                String strIp = (String) ips[index];
                if (!("unknown".equalsIgnoreCase(strIp))) {
                    ip = strIp;
                    break;
                }
            }
        }
        return ip;
    }
}