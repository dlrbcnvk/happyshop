package com.project.happyshop.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
@Slf4j
public class HomeController {

    @GetMapping("/")
    public String index(HttpSession session) {
        log.info("Session id = {}", session.getId());
        log.info("Session = {}", session);
        return "index";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/login")
    public String login(HttpSession session) {
        log.info("Session id = {}", session.getId());
        log.info("Session = {}", session);
        return "login";
    }

    @PostMapping("/login")
    public String login_post() {
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout() {
        return "redirect:/";
    }
}
