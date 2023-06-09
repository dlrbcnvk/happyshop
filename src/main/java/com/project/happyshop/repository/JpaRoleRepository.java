package com.project.happyshop.repository;

import com.project.happyshop.domain.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaRoleRepository extends JpaRepository<Role, Long> {

    Role findByRoleName(String roleName);
}
