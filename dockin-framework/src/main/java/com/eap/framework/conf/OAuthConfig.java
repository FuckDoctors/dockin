package com.eap.framework.conf;

import com.eap.framework.oauth.common.CustomOAuthService;
import com.eap.framework.oauth.common.OAuthTypes;
import com.eap.framework.oauth.esp.EspApi;
import com.eap.framework.oauth.github.GithubApi;
import org.scribe.builder.ServiceBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by billJiang on 2017/1/15.
 * e-mail:jrn1012@petrochina.com.cn qq:475572229
 */
@Configuration
public class OAuthConfig {
    @Value("${oauth.callback.url}")
    String callback_url;

    /**
     * github配置
     */
    @Value("${oauth.github.key}")
    String github_key;
    @Value("${oauth.github.secret}")
    String github_secret;
    @Value("${oauth.github.state}")
    String github_state;

    @Bean
    public GithubApi githubApi() {
        return new GithubApi(github_state);
    }

    @Bean
    public CustomOAuthService getGithubOAuthService() {
        return (CustomOAuthService) new ServiceBuilder()
                .provider(githubApi())
                .apiKey(github_key)
                .apiSecret(github_secret)
                .callback(String.format(callback_url, OAuthTypes.GITHUB))
                .build();
    }


    //------oa  add by billjiang
    @Value("${oauth.oa.key}")
    String oa_key;
    @Value("${oauth.oa.secret}")
    String oa_secret;
    @Value("${oauth.oa.scope}")
    String oa_scope;
    @Value("${oauth.oa.btnclass}")
    String oa_btnclass;
    @Value("${oauth.oa.authorize_url}")
    String oa_authorize_url;
    @Value("${oauth.oa.access_token_url}")
    String oa_access_token_url;
    @Value("${oauth.oa.user_url}")
    String oa_user_url;

    @Bean
    public EspApi espApi(){
        return new EspApi(oa_authorize_url,oa_access_token_url,oa_user_url,oa_btnclass);
    }

    @Bean
    public CustomOAuthService getEspOAuthService(){
        return (CustomOAuthService) new ServiceBuilder()
                .provider(espApi())
                .apiKey(oa_key)
                .apiSecret(oa_secret)
                .scope(oa_scope)
                .callback(String.format(callback_url,OAuthTypes.OA))
                .build();
    }



}
