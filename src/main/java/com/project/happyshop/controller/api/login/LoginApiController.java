package com.project.happyshop.controller.api.login;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")
@Slf4j
public class LoginApiController {

    @GetMapping("/login")
    public String login_api() {
        log.info("GET /api/login");
        return "POST /login 을 요청하세요.";
    }

    @PostMapping("/login")
    public Object login_post_api() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return auth;
    }

    @GetMapping("/logout")
    public String logout_api(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        log.info("GET /api/logout");
        return "logout success";
    }
}
