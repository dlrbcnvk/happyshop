package com.project.happyshop.security.service;

import com.project.happyshop.domain.entity.Member;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class UserDetail extends User {

    private Member member;
    private List<String> roles;

    public UserDetail(Member member, List<String> roles) {
        super(member.getUsername(), member.getPassword(), roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        this.member = member;
        this.roles = roles;
    }
}
