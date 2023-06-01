package com.project.happyshop.repository;

import com.project.happyshop.domain.entity.Item;
import com.project.happyshop.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaItemRepository extends JpaRepository<Item, Long> {

    List<Item> findBySeller(Member member);
}
