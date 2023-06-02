package com.project.happyshop.service;

import com.project.happyshop.domain.entity.Role;
import com.project.happyshop.repository.JpaRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RoleService {

    private final JpaRoleRepository jpaRoleRepository;

    public void save(Role role) {
        jpaRoleRepository.save(role);
    }

    public Role findByRoleName(String roleName) {
        return jpaRoleRepository.findByRoleName(roleName);
    }

    public Role getRole(long id) {
        return jpaRoleRepository.findById(id).orElse(new Role());
    }

    public List<Role> getRoles() {
        return jpaRoleRepository.findAll();
    }

    public void createRole(Role role) {
        jpaRoleRepository.save(role);
    }
}
