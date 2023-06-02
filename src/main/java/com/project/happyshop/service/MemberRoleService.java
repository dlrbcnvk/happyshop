package com.project.happyshop.service;

import com.project.happyshop.domain.entity.MemberRole;
import com.project.happyshop.repository.JpaMemberRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberRoleService {

    private final JpaMemberRoleRepository jpaMemberRoleRepository;

    public void save(MemberRole memberRole) {
        jpaMemberRoleRepository.save(memberRole);
    }
}
