package com.eap.framework.wxtools.handler;

import com.eap.framework.wxtools.exception.WxErrorException;

/**
 * 微信异常处理器接口
 * 
 * @author antgan
 *
 */
public interface WxErrorExceptionHandler {

	public void handle(WxErrorException e);

}
