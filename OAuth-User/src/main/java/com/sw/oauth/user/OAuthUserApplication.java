package com.sw.oauth.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

/**
 * @author suaxi
 * @date 2022/2/14 22:08
 */
@SpringBootApplication
@EnableResourceServer
public class OAuthUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(OAuthUserApplication.class, args);
    }
}
