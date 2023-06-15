package com.project.happyshop.repository;

import com.project.happyshop.domain.entity.Item;
import com.project.happyshop.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaItemRepository extends JpaRepository<Item, Long> {

    List<Item> findBySeller(Member member);


    List<Item> findBySellerNot(Member member);
}
