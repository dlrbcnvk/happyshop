package com.project.happyshop.repository;

import com.project.happyshop.domain.entity.Member;
import com.project.happyshop.domain.entity.MemberRole;
import com.project.happyshop.domain.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMemberRoleRepository extends JpaRepository<MemberRole, Long> {
    MemberRole findByMemberAndRole(Member member, Role role);
}
