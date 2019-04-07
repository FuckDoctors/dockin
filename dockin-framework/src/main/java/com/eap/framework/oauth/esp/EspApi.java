package com.eap.framework.oauth.esp;

import org.scribe.builder.api.DefaultApi20;
import org.scribe.model.OAuthConfig;
import org.scribe.oauth.OAuthService;
import org.scribe.utils.OAuthEncoder;

/**
 * @author billjiang 475572229@qq.com
 * @create 18-4-6
 */
public class EspApi extends DefaultApi20 {

    private String authorize_url;
    private String access_token_url;
    private String user_url;
    private String btn_class;

    public EspApi(String authorize_url,String access_token_url,String user_url,String btn_class){
       this.authorize_url=authorize_url;
       this.access_token_url=access_token_url;
       this.user_url=user_url;
       this.btn_class=btn_class;
    }
    @Override
    public String getAccessTokenEndpoint() {
       return this.access_token_url;
    }


    @Override
    public String getAuthorizationUrl(OAuthConfig config) {
        return String.format(this.authorize_url,config.getApiKey(), OAuthEncoder.encode(config.getCallback()),config
                .getScope());
    }

    @Override
    public OAuthService createService(OAuthConfig config){
        return new EspOAuthService(this,config);
    }

    public String  getUserUrl(){
        return this.user_url;
    }

    public String getBtnClass(){
        return this.btn_class;
    }
}
