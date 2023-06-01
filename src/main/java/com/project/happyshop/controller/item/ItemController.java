package com.project.happyshop.controller.item;

import com.project.happyshop.domain.dto.RegisterItemDto;
import com.project.happyshop.domain.entity.Item;
import com.project.happyshop.domain.entity.Member;
import com.project.happyshop.service.ItemService;
import com.project.happyshop.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemService itemService;
    private final MemberService memberService;

    // 판매자 상품 조회 + 뷰
    @GetMapping("/items/seller")
    public String seller(@AuthenticationPrincipal Member member) {

        Member findMember = memberService.findOne(member.getId());
        List<Item> itemList = itemService.findByMember(findMember);

        for (Item item : itemList) {
            log.info(item.toString());
        }

        return "item/seller";
    }

    @GetMapping("/items/add")
    public String addItem(@AuthenticationPrincipal Member member, Model model) {
        model.addAttribute("member", member);
        return "item/addItem";
    }

    @PostMapping("/items/add")
    public String addItem(@AuthenticationPrincipal Member member, RegisterItemDto itemDto) {

        log.info("판매자 이름={}", member.getUsername());
        log.info("상품등록={}", itemDto.toString());

        Member findMember = memberService.findOne(member.getId());

        Item item = Item.createItem(
                itemDto.getItemName(),
                itemDto.getPrice(),
                itemDto.getQuantity(),
                itemDto.getDescription(),
                itemDto.getImageUrl());
        item.setSeller(findMember);
        itemService.save(item);

        return "redirect:/items/seller";
    }
}
