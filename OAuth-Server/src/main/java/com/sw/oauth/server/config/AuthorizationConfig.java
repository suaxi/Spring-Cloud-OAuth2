package com.sw.oauth.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

/**
 * @author suaxi
 * @date 2022/2/14 22:11
 */
@Configuration
public class AuthorizationConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthorizationCodeServices authorizationCodeServices;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private JwtAccessTokenConverter accessTokenConverter;

    @Autowired
    private ClientDetailsService clientDetailsService;

    /**
     * 3.????????????????????????
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                //oauth/token_key ??????
                .tokenKeyAccess("permitAll()")
                //oauth/check_token ??????
                .checkTokenAccess("permitAll()")
                //???????????????????????????
                .allowFormAuthenticationForClients();
    }

    /**
     * 1.???????????????
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                //clientId
                .withClient("c1")
                //???????????????
                .secret(new BCryptPasswordEncoder().encode("secret"))
                //????????????
                .resourceIds("admin")
                //????????????
                .authorizedGrantTypes("authorization_code", "password", "client_credentials", "implicit", "refresh_token")
                //????????????
                .scopes("all")
                //?????????????????????
                .autoApprove(false)
                //????????????
                .redirectUris("https://wangchouchou.com");
    }

    /**
     * 2.????????????
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                //???????????????
                .authenticationManager(authenticationManager)
                //?????????????????????????????????
                .userDetailsService(userDetailsService)
                //???????????????
                .authorizationCodeServices(authorizationCodeServices)
                //??????????????????
                .tokenServices(tokenServices())
                .allowedTokenEndpointRequestMethods(HttpMethod.POST);
    }

    public AuthorizationServerTokenServices tokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        //???????????????
        tokenServices.setClientDetailsService(clientDetailsService);
        //????????????????????????
        tokenServices.setSupportRefreshToken(true);
        //??????????????????
        tokenServices.setTokenStore(tokenStore);
        //TODO
        //??????JWT??????
        tokenServices.setTokenEnhancer(accessTokenConverter);
        //?????????????????????
        tokenServices.setAccessTokenValiditySeconds(3600);
        //?????????????????????
        tokenServices.setRefreshTokenValiditySeconds(86400);
        return tokenServices;
    }

    /**
     * ???????????????????????????????????????
     * @return
     */
    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        return new InMemoryAuthorizationCodeServices();
    }
}
