package com.project.happyshop.service;

import com.project.happyshop.domain.entity.Item;
import com.project.happyshop.domain.entity.Member;
import com.project.happyshop.repository.JpaItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {

    private final JpaItemRepository jpaItemRepository;

    public void save(Item item) {
        jpaItemRepository.save(item);
    }

    @Transactional(readOnly = true)
    public List<Item> findByMember(Member member) {
        return jpaItemRepository.findBySeller(member);
    }

    @Transactional(readOnly = true)
    public Item findById(Long itemId) throws NoSuchElementException {
        return jpaItemRepository.findById(itemId).orElseThrow();
    }

    public void deleteItem(Item item) {
        jpaItemRepository.delete(item);
    }
}
