package com.project.happyshop.domain;

import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    /**
     * busniess key: (email, provider)
     */
    private String email;

    @Enumerated(EnumType.STRING)
    private SocialProvider provider;

    private String password;
    private String username;
    private String phoneNumber;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member")
    private List<Order> orderList = new ArrayList<>();

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
