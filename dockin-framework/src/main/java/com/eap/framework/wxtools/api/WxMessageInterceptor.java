package com.eap.framework.wxtools.api;

import java.util.Map;

import com.eap.framework.wxtools.bean.WxXmlMessage;
import com.eap.framework.wxtools.exception.WxErrorException;
import com.eap.framework.wxtools.service.WxService;

/**
 * <pre>
 * 微信消息拦截器
 * 
 * 实现该接口可以实现消息验证等功能
 * </pre>
 *
 * @author antgan
 */
public interface WxMessageInterceptor {

	/**
	 * 拦截微信消息
	 *
	 * @param wxMessage
	 * @param context
	 *            上下文，如果handler或interceptor之间有信息要传递，可以用这个
	 * @param iService
	 * @return true代表允许通过，false代表不允许通过
	 */
	public boolean intercept(WxXmlMessage wxMessage, Map<String, Object> context, WxService iService)
			throws WxErrorException;

}
