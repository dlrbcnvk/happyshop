package com.project.happyshop.repository;

import com.project.happyshop.domain.entity.Resource;
import com.project.happyshop.domain.entity.Role;
import com.project.happyshop.domain.entity.RoleResource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaRoleResourceRepository extends JpaRepository<RoleResource, Long> {
    RoleResource findByRoleAndResource(Role role, Resource resource);
}
