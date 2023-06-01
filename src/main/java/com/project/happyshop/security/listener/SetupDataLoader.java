package com.project.happyshop.security.listener;

import com.project.happyshop.domain.Address;
import com.project.happyshop.domain.SocialProvider;
import com.project.happyshop.domain.entity.*;
import com.project.happyshop.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    private final JpaRoleRepository jpaRoleRepository;
    private final JpaResourceRepository jpaResourceRepository;
    private final JpaRoleResourceRepository jpaRoleResourceRepository;
    private final JpaMemberRepository jpaMemberRepository;
    private final MemberRepository memberRepository;
    private final JpaMemberRoleRepository jpaMemberRoleRepository;

    private final PasswordEncoder passwordEncoder;

    private static AtomicInteger count = new AtomicInteger(0);

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup) {
            return;
        }

        setupSecurityResources();

        alreadySetup = true;
    }

    // TODO username, password 정보들을 설정파일(application.yml -> gitignore)에 따로 둬서 감춰야함
    private void setupSecurityResources() {
        Set<Role> roles1 = new HashSet<>();
        Role adminRole = createRoleIfNotFound("ROLE_ADMIN", "관리자");
        roles1.add(adminRole);
        createResourceIfNotFound("/admin/**", "", roles1, "url");
        createMemberIfNotFound(
                "admin",
                passwordEncoder.encode("pass"),
                "admin@gmail.com",
                SocialProvider.LOCAL,
                "010-1234-1234",
                new Address("juso", "jusoDetail", "12345"),
                roles1);

        Set<Role> roles2 = new HashSet<>();
        Role managerRole = createRoleIfNotFound("ROLE_MANAGER", "매니저");
        roles2.add(managerRole);
        createMemberIfNotFound(
                "manager",
                passwordEncoder.encode("pass"),
                "manager@gmail.com",
                SocialProvider.LOCAL,
                "010-1234-1234",
                new Address("juso", "jusoDetail", "12345"),
                roles2);


        Set<Role> roles3 = new HashSet<>();
        Role memberRole = createRoleIfNotFound("ROLE_USER", "회원");
        roles3.add(memberRole);
        createResourceIfNotFound("/members/**", "", roles3, "url");
        createMemberIfNotFound(
                "user",
                passwordEncoder.encode("pass"),
                "user@gmail.com",
                SocialProvider.LOCAL,
                "010-1234-1234",
                new Address("juso", "jusoDetail", "12345"),
                roles3);
    }

    @Transactional
    public Member createMemberIfNotFound(String username, String password, String email, SocialProvider provider, String phoneNumber, Address address, Set<Role> roles) {
        List<Member> memberList = memberRepository.findByEmailAndProvider(email, provider);
        Member member;
        if (memberList.size() == 0) {
            member = Member.createMember(email, provider, username, password, phoneNumber, address);
            jpaMemberRepository.save(member);
        } else {
            member = memberList.get(0);
        }

        if (member != null) {
            Set<MemberRole> memberRoles = member.getMemberRoles();
            Set<MemberRole> collect = roles.stream()
                    .map(role -> createMemberRoleIfNotFound(member, role))
                    .collect(Collectors.toSet());
            memberRoles.addAll(collect);
        }

        return member;
    }

    @Transactional
    public MemberRole createMemberRoleIfNotFound(Member member, Role role) {
        MemberRole memberRole = jpaMemberRoleRepository.findByMemberAndRole(member, role);
        if (memberRole == null) {
            memberRole = new MemberRole();
            memberRole.setMemberAndRole(member, role);
            jpaMemberRoleRepository.save(memberRole);
        }
        return memberRole;
    }


    @Transactional
    public Role createRoleIfNotFound(String roleName, String roleDesc) {

        Role role = jpaRoleRepository.findByRoleName(roleName);

        if (role == null) {
            role = new Role();
            role.setNameAndDesc(roleName, roleDesc);
        }
        return jpaRoleRepository.save(role);
    }

    @Transactional
    public Resource createResourceIfNotFound(String resourceName, String httpMethod, Set<Role> roleSet, String resourceType) {
        Resource resource = jpaResourceRepository.findByResourceNameAndHttpMethod(resourceName, httpMethod);

        if (resource == null) {
            resource = new Resource();
            resource.setResourceInfos(resourceName, httpMethod, count.incrementAndGet(), resourceType);
        }
        jpaResourceRepository.save(resource);
        for (Role role : roleSet) {
            RoleResource roleResource = createRoleResourceIfNotFound(role, resource);
            resource.addRoleResource(roleResource);
        }
        return resource;
    }

    @Transactional
    public RoleResource createRoleResourceIfNotFound(Role role, Resource resource) {
        RoleResource roleResource = jpaRoleResourceRepository.findByRoleAndResource(role, resource);
        if (roleResource == null) {
            roleResource = new RoleResource();
            roleResource.setRoleAndResource(role, resource);
            jpaRoleResourceRepository.save(roleResource);
        }
        return roleResource;
    }
}
