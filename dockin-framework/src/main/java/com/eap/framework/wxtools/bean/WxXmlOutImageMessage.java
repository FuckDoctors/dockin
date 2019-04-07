package com.eap.framework.wxtools.bean;

import com.eap.framework.wxtools.api.WxConsts;
import com.eap.framework.wxtools.util.xml.XStreamMediaIdConverter;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

/**
 * <pre>
 * 被动回复消息--回复图片消息体
 * 
 * 详情:http://mp.weixin.qq.com/wiki/1/6239b44c206cab9145b1d52c67e6c551.html
 * </pre>
 * @author antgan
 *
 */
@XStreamAlias("xml")
public class WxXmlOutImageMessage extends WxXmlOutMessage {

	@XStreamAlias("Image")
	@XStreamConverter(value = XStreamMediaIdConverter.class)
	private String mediaId;

	public WxXmlOutImageMessage() {
		this.msgType = WxConsts.XML_MSG_IMAGE;
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

}
