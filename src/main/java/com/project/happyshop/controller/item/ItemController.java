package com.project.happyshop.controller.item;

import com.project.happyshop.domain.dto.ItemDto;
import com.project.happyshop.domain.entity.Item;
import com.project.happyshop.domain.entity.Member;
import com.project.happyshop.service.ItemService;
import com.project.happyshop.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemService itemService;
    private final MemberService memberService;

    // 판매자 상품 조회 + 뷰
    @GetMapping("/items/seller")
    public String seller(@AuthenticationPrincipal Member member, Model model) {

        Member findMember = memberService.findOne(member.getId());
        List<Item> itemList = findMember.getItems();

        model.addAttribute("itemList", itemList);


        return "item/seller";
    }

    @GetMapping("/items/add")
    public String addItem(@AuthenticationPrincipal Member member, Model model) {
        model.addAttribute("member", member);
        return "item/addItem";
    }

    @PostMapping("/items/add")
    public String addItem(@AuthenticationPrincipal Member member, ItemDto itemDto) {

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

    @GetMapping("/items/update/{itemId}")
    public String updateItem(
            @AuthenticationPrincipal Member member,
            @PathVariable("itemId") Long itemId,
            Model model) {
        try {
            Item findItem = itemService.findById(itemId);
            Member findMember = memberService.findOne(member.getId());
            model.addAttribute("item", findItem);
            model.addAttribute("member", findMember);

        } catch (NoSuchElementException e) {
            log.info(e.toString());
        }

        return "item/updateItem";
    }

    @Transactional
    @PostMapping("/items/update/{itemId}")
    public String updateItem(
            @PathVariable("itemId") Long itemId,
            ItemDto itemDto) {

        Item findItem = itemService.findById(itemId);
        findItem.updateItem(itemDto);

        return "redirect:/items/seller";
    }

    @Transactional
    @PostMapping("/items/delete/{itemId}")
    public String deleteItem(
            @AuthenticationPrincipal Member member,
            @PathVariable("itemId") Long itemId
    ) {
        Member findMember = memberService.findOne(member.getId());
        Item findItem = itemService.findById(itemId);

        if (findItem.getSeller() == findMember) {
            itemService.deleteItem(findItem);
        }

        return "redirect:/items/seller";
    }

    @GetMapping("/items/detail/{itemId}")
    public String itemDetail(@PathVariable("itemId") Long itemId, Model model) {
        Item findItem = itemService.findById(itemId);
        model.addAttribute("item", findItem);
        return "/item/itemDetail";
    }
}
