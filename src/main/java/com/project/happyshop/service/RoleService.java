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
@RequiredArgsConstructor
public class RoleService {

    private final JpaRoleRepository jpaRoleRepository;

    @Transactional
    public Role getRole(long id) {
        return jpaRoleRepository.findById(id).orElse(new Role());
    }

    @Transactional
    public List<Role> getRoles() {
        return jpaRoleRepository.findAll();
    }

    @Transactional
    public void createRole(Role role) {
        jpaRoleRepository.save(role);
    }
}
