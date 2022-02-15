package com.sw.oauth.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author suaxi
 * @date 2022/2/15 22:43
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/test")
    public String test() {
        return "test";
    }
}
