package com.eap.framework.wxtools.handler;

import java.util.Map;

import com.eap.framework.wxtools.service.WxService;
import com.eap.framework.wxtools.api.WxMessageHandler;
import com.eap.framework.wxtools.bean.WxXmlMessage;
import com.eap.framework.wxtools.bean.WxXmlOutMessage;
import com.eap.framework.wxtools.exception.WxErrorException;

/**
 * 示例：DemoHandler
 * 目的：返回用户 “你好”
 * @author antgan
 * @date 2016/12/15
 *
 */
public class DemoHandler implements WxMessageHandler{

	@Override
	public WxXmlOutMessage handle(WxXmlMessage wxMessage, Map<String, Object> context, WxService iService)
			throws WxErrorException {
        //必须以build()作为结尾，否则不生效。
		WxXmlOutMessage xmlOutMsg = WxXmlOutMessage.TEXT().content("你好").toUser(wxMessage.getFromUserName()).fromUser(wxMessage.getToUserName()).build();
		return xmlOutMsg;
	}
	
}
