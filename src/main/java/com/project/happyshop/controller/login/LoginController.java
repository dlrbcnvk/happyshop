package com.project.happyshop.controller.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @PostMapping("/login")
    public String login_post() {
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout() {
        return "redirect:/";
    }
}
