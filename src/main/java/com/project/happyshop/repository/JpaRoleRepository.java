package com.project.happyshop.repository;

import com.project.happyshop.domain.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaRoleRepository extends JpaRepository<Role, Long> {

    Role findByRoleName(String roleName);
}
