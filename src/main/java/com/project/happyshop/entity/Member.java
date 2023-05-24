package com.project.happyshop.entity;

import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    /**
     * busniess key: (username, provider)
     */
    private String username;

    @Enumerated(EnumType.STRING)
    private SocialProvider provider;

    private String email;
    private String password;
    private String phoneNumber;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member")
    private List<Order> orderList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private Set<MemberRole> memberRoles = new HashSet<>();

    public static Member createMember(
            String email,
            SocialProvider provider,
            String password,
            String username,
            String phoneNumber,
            Address address
    ) {
        Member member = new Member();
        member.email = email;
        member.provider = provider;
        member.password = password;
        member.username = username;
        member.phoneNumber = phoneNumber;
        member.address = address;
        return member;
    }

    public Long updateMember(String email, String password, String username, String phoneNumber, Address address) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.address = address;
        return this.id;
    }
}
