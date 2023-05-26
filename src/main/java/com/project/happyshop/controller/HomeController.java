package com.project.happyshop.controller;

import com.project.happyshop.domain.entity.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
@Slf4j
public class HomeController {

    @GetMapping("/")
    public String index(@AuthenticationPrincipal Member member, Model model) {
        if (member != null) {
            model.addAttribute("member", member);
        }
        return "index";
    }

}
