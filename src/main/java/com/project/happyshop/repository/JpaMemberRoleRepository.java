package com.project.happyshop.repository;

import com.project.happyshop.domain.entity.Member;
import com.project.happyshop.domain.entity.MemberRole;
import com.project.happyshop.domain.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaMemberRoleRepository extends JpaRepository<MemberRole, Long> {
    MemberRole findByMemberAndRole(Member member, Role role);
}
