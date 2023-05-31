package com.project.happyshop.repository;

import com.project.happyshop.domain.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaItemRepository extends JpaRepository<Item, Long> {
}
