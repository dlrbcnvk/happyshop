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
    public String index() {
        return "index";
    }



    @GetMapping("/login")
    public String login() {
        return "login";
    }


}
