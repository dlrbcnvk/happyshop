package com.project.happyshop.controller.api;

import com.project.happyshop.domain.entity.Item;
import com.project.happyshop.domain.entity.Member;
import com.project.happyshop.security.model.PrincipalUser;
import com.project.happyshop.service.ItemService;
import com.project.happyshop.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class HomeApiController {

    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping("/")
    public String index(
            @AuthenticationPrincipal PrincipalUser principalUser,
            Model model,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {

        List<Item> itemList;
        if (principalUser != null) {
            // 회원이거나, 회원은 아닌데 소셜로그인 버튼을 누른 사람
            Member findMember = memberService.findByEmailAndProvider(principalUser.getEmail(), principalUser.getSocialProvider());
            if (findMember == null) {
                // 회원은 아닌데 소셜로그인 버튼을 누른 사람
                // 회원가입 페이지로 이동해야 함.
                redirectAttributes.addAttribute("email", principalUser.getEmail());
                redirectAttributes.addAttribute("username", principalUser.getUsername());

                return "redirect:/register/social/{email}/{username}";
            } else {
                // 회원
                itemList = itemService.findBySellerNot(findMember);
                model.addAttribute("member", findMember);
            }

        } else {
            itemList = itemService.findAll();
        }
        model.addAttribute("itemList", itemList);
        return "ok";
    }
}
