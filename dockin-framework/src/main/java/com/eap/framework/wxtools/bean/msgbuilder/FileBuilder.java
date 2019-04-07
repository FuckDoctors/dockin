package com.eap.framework.wxtools.bean.msgbuilder;

import com.eap.framework.wxtools.api.WxConsts;
import com.eap.framework.wxtools.bean.WxMessage;

/**
 * 获得消息builder
 * 
 * <pre>
 * 用法: WxCustomMessage m = WxCustomMessage.FILE().mediaId(...).toUser(...).build();
 * </pre>
 * 
 * @author antgan
 *
 */
public final class FileBuilder extends BaseBuilder<FileBuilder> {
	private String mediaId;

	public FileBuilder() {
		this.msgType = WxConsts.CUSTOM_MSG_FILE;
	}

	public FileBuilder mediaId(String media_id) {
		this.mediaId = media_id;
		return this;
	}

	public WxMessage build() {
		WxMessage m = super.build();
		m.setMediaId(this.mediaId);
		return m;
	}
}
