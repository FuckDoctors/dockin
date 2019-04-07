package com.eap.framework.wxtools.bean.msgbuilder;

import com.eap.framework.wxtools.api.WxConsts;
import com.eap.framework.wxtools.bean.WxMessage;

/**
 * 语音消息builder
 * <pre>
 * 用法: WxCustomMessage m = WxCustomMessage.VOICE().mediaId(...).toUser(...).build();
 * </pre>
 * @author antgan
 *
 */
public final class VoiceBuilder extends BaseBuilder<VoiceBuilder> {
  private String mediaId;

  public VoiceBuilder() {
    this.msgType = WxConsts.CUSTOM_MSG_VOICE;
  }

  public VoiceBuilder mediaId(String media_id) {
    this.mediaId = media_id;
    return this;
  }

  public WxMessage build() {
    WxMessage m = super.build();
    m.setMediaId(this.mediaId);
    return m;
  }
}
