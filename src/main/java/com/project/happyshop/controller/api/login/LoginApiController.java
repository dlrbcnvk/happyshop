package com.project.happyshop.controller.api.login;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")
@Slf4j
public class LoginApiController {

    @GetMapping("/login")
    public String login_api() {
        log.info("GET /api/login");
        return "login";
    }

    @PostMapping("/login")
    public String login_post_api() {
        log.info("POST /api/login");
        return "login post success";
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
