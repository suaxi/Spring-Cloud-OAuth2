package com.sw.oauth2authserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

import javax.sql.DataSource;


/**
 * @Description
 * @Author Wang Hao
 * @Date 2021/8/17 17:15
 */
@Configuration
@EnableAuthorizationServer
public class OAuth2Config extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenStore redisTokenStore;

    @Autowired
    private DataSource dataSource;

    @Override
    public void configure(final AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        //密码模式授权管理Bean
        endpoints.authenticationManager(authenticationManager)
                //用户验证服务
                .userDetailsService(userDetailsService)
                //指定token存储方式
                .tokenStore(redisTokenStore);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.jdbc(dataSource);
//        clients.inMemory()
//                .withClient("order-client")
//                .secret(passwordEncoder.encode("order_123456"))
//                //授权模式
//                .authorizedGrantTypes("refresh_token", "authorization_code", "password")
//                //token有效期
//                .accessTokenValiditySeconds(3600)
//                //客户端访问权限
//                .scopes("all")
//                .and()
//                .withClient("user-client")
//                .secret(passwordEncoder.encode("user_123456"))
//                .authorizedGrantTypes("refresh_token", "authorization_code", "password")
//                .accessTokenValiditySeconds(3600)
//                .scopes("all")
//                .and()
//                //授权码模式
//                .withClient("code-client")
//                .secret(passwordEncoder.encode("code_123456"))
//                .authorizedGrantTypes("authorization_code", "refresh_token")
//                .accessTokenValiditySeconds(3600)
//                .redirectUris("http://localhost:8090/code/login")
//                .scopes("all");
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        //允许客户端访问OAuth2授权接口
        security.allowFormAuthenticationForClients();
        //允许已授权用户访问chekToken和获取token接口
        security.checkTokenAccess("isAuthenticated()");
        security.tokenKeyAccess("isAuthenticated()");
    }
}
