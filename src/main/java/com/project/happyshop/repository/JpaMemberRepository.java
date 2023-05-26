package com.project.happyshop.repository;

import com.project.happyshop.domain.entity.Member;
import com.project.happyshop.domain.SocialProvider;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface JpaMemberRepository extends JpaRepository<Member, Long> {

    @EntityGraph(attributePaths = {"orderList", "memberRoles"})
    Member findByUsernameAndProvider(String username, SocialProvider provider);
}
