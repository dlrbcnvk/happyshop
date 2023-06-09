package com.project.happyshop.controller;

import com.project.happyshop.domain.entity.Item;
import com.project.happyshop.domain.entity.Member;
import com.project.happyshop.domain.entity.MemberRole;
import com.project.happyshop.security.model.PrincipalUser;
import com.project.happyshop.service.ItemService;
import com.project.happyshop.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Controller
@Slf4j
@RequiredArgsConstructor
public class HomeController {

    private final ItemService itemService;
    private final MemberService memberService;

    @GetMapping("/")
    public String index(@AuthenticationPrincipal PrincipalUser principalUser, Model model) {

        List<Item> itemList;
        if (principalUser != null) {
            // 회원이거나, 회원은 아닌데 소셜로그인 버튼을 누른 사람
            Member findMember = memberService.findByEmailAndProvider(principalUser.getEmail(), principalUser.getSocialProvider());
            if (findMember == null) {
                // 회원은 아닌데 소셜로그인 버튼을 누른 사람
                // 회원가입 페이지로 이동해야 함.
                model.addAttribute("email", principalUser.getEmail());
                model.addAttribute("username", principalUser.getUsername());
                return "/member/login/register";
            } else {
                // 회원
                itemList = itemService.findBySellerNot(findMember);
                model.addAttribute("member", findMember);
            }

        } else {
            itemList = itemService.findAll();
        }
        model.addAttribute("itemList", itemList);
        return "index";
    }

}
