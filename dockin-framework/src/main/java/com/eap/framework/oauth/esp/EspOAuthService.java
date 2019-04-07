package com.eap.framework.oauth.esp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import com.eap.framework.oauth.common.CustomOAuthService;
import com.eap.framework.oauth.common.OAuthTypes;
import com.eap.framework.oauth.entity.OAuthUser;
import org.scribe.model.*;
import org.scribe.oauth.OAuth20ServiceImpl;

/**
 * @author billjiang 475572229@qq.com
 * @create 18-4-6
 */
public class EspOAuthService extends OAuth20ServiceImpl implements CustomOAuthService {

    private String userUrl;
    private String btnClass;
    private final EspApi api;
    private final OAuthConfig config;
    private final String authorizationUrl;

    public EspOAuthService(EspApi api, OAuthConfig config) {
        super(api,config);
        this.api=api;
        this.config=config;
        this.authorizationUrl=this.api.getAuthorizationUrl(config);
        this.userUrl=this.api.getUserUrl();
        this.btnClass=this.api.getBtnClass();
    }


    @Override
    public String getOAuthType() {
        return OAuthTypes.OA;
    }

    @Override
    public String getAuthorizationUrl() {
        return this.authorizationUrl;
    }

    @Override
    public OAuthUser getOAuthUser(Token accessToken) {
        OAuthRequest request = new OAuthRequest(Verb.GET, this.userUrl);
        this.signRequest(accessToken, request);
        Response response = request.send();
        OAuthUser oAuthUser = new OAuthUser();
        oAuthUser.setoAuthType(getOAuthType());
        Object result = JSON.parse(response.getBody());
        oAuthUser.setoAuthId(JSONPath.eval(result, "$.id").toString());
        oAuthUser.setUserName(JSONPath.eval(result, "$.login").toString());
        return oAuthUser;
    }

    @Override
    public String getBtnClass() {
        return this.btnClass;
    }


}
