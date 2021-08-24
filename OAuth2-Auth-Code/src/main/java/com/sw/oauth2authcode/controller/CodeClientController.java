package com.sw.oauth2authcode.controller;

import com.alibaba.fastjson.JSONObject;
import com.sw.oauth2authcode.pojo.UserDemo;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.Map;

/**
 * @Description
 * @Author Wang Hao
 * @Date 2021/8/24 14:58
 */
@Slf4j
@Controller
public class CodeClientController {

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public Object login(String code, Model m) {
        String tokenUrl = "http://localhost:8088/oauth/token";
        OkHttpClient httpClient = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("grant_type", "authorization_code")
                .add("client", "code-client")
                .add("redirect_uri", "http://localhost:8090/code/login")
                .add("code", code)
                .build();

        Request request = new Request.Builder()
                .url(tokenUrl)
                .post(body)
                .addHeader("Authorization", "Basic Y29kZS1jbGllbnQ6Y29kZV8xMjM0NTY=")
                .build();

        try {
            Response resp = httpClient.newCall(request).execute();
            String result = resp.body().string();
            Map<String, Object> map = JSONObject.parseObject(result);
            String accessToken = map.get("access_token").toString();
            m.addAttribute("username", getUserName());
            m.addAttribute("accessToken", accessToken);
            return "index";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDemo){
            return ((UserDemo) principal).getUsername();
        }
        return null;
    }
}
