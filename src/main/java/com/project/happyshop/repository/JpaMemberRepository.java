package com.project.happyshop.repository;

import com.project.happyshop.entity.Member;
import com.project.happyshop.entity.SocialProvider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMemberRepository extends JpaRepository<Member, Long> {

    Member findByUsernameAndProvider(String username, SocialProvider provider);
}
