package com.project.happyshop.controller.api.item;

import com.project.happyshop.domain.dto.ItemDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/items")
public class ItemApiController {

    @PostMapping("/add")
    public Object addItem(@RequestBody @Validated ItemDto itemDto, BindingResult bindingResult) {
        log.info("API 호출");
        if (bindingResult.hasErrors()) {
            log.info("검증 오류 발생. errors={}", bindingResult);
            return bindingResult.getAllErrors();
        }

        log.info("성공 로직 실행. 회원 인증하고 db 에 insert 하면 됨. 지금은 테스트 메서드.");
        return itemDto;
    }
}
