package com.project.happyshop.service;

import com.project.happyshop.domain.entity.Item;
import com.project.happyshop.domain.entity.Member;
import com.project.happyshop.repository.JpaItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final JpaItemRepository jpaItemRepository;
    private final MemberService memberService;

    @Transactional
    public void save(Item item, Member member) {

    }
}
