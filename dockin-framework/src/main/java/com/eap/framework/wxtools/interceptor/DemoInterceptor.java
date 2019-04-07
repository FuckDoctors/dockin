package com.eap.framework.wxtools.interceptor;

import java.util.Map;

import com.eap.framework.wxtools.service.WxService;
import com.eap.framework.wxtools.api.WxConsts;
import com.eap.framework.wxtools.api.WxMessageInterceptor;
import com.eap.framework.wxtools.bean.WxXmlMessage;
import com.eap.framework.wxtools.exception.WxErrorException;

/**
 * 示例：拦截器
 * 目的：拦截非TEXT类型的所有消息
 * @author antgan
 * @date 2016/12/15
 *
 */
public class DemoInterceptor implements WxMessageInterceptor {

	@Override
	public boolean intercept(WxXmlMessage wxMessage, Map<String, Object> context, WxService iService)
			throws WxErrorException {
		//拦截所有非TEXT类型的消息,true通行；false拦截
		if(WxConsts.XML_MSG_TEXT.equals(wxMessage.getMsgType())){
			return true;
		}
		return false;
	}
}
