package com.project.happyshop.controller;

import com.project.happyshop.domain.entity.Item;
import com.project.happyshop.domain.entity.Member;
import com.project.happyshop.domain.entity.MemberRole;
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
import java.util.List;
import java.util.Set;

@Controller
@Slf4j
@RequiredArgsConstructor
public class HomeController {

    private final ItemService itemService;
    private final MemberService memberService;

    @GetMapping("/")
    public String index(@AuthenticationPrincipal Member member, Model model) {
        List<Item> itemList;
        if (member != null) {
            model.addAttribute("member", member);
            Member findMember = memberService.findOne(member.getId());
            itemList = itemService.findBySellerNot(findMember);
        } else {
            itemList = itemService.findAll();
        }
        model.addAttribute("itemList", itemList);
        return "index";
    }

}
