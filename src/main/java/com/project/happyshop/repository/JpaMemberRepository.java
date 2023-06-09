package com.project.happyshop.repository;

import com.project.happyshop.domain.entity.Member;
import com.project.happyshop.domain.SocialProvider;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaMemberRepository extends JpaRepository<Member, Long> {

    @EntityGraph(attributePaths = {"orderList", "memberRoles"})
    Member findByEmailAndProvider(String email, SocialProvider provider);

    /**
     * LOCAL form 인증 서비스에서만 사용할 것.
     * UserDetailsServiceImpl
     */
    @EntityGraph(attributePaths = {"orderList", "memberRoles"})
    Member findByUsernameAndProvider(String username, SocialProvider provider);
}
