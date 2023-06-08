package com.project.happyshop.domain.entity;

import com.project.happyshop.domain.Address;
import com.project.happyshop.domain.SocialProvider;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@ToString(exclude = {"password", "orderList", "memberRoles", "items"})
public class Member implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    /**
     * busniess key: (email, provider)
     */
    private String username;

    @Enumerated(EnumType.STRING)
    private SocialProvider provider;

    private String email;
    private String password;
    private String phoneNumber;
    private String socialId;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Order> orderList = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private Set<MemberRole> memberRoles = new HashSet<>();

    @OneToMany(mappedBy = "seller", fetch = FetchType.LAZY)
    private List<Item> items = new ArrayList<>();

    public Member() {

    }

    public static Member createMember(
            String email,
            SocialProvider provider,
            String username,
            String password,
            String phoneNumber,
            Address address
    ) {
        Member member = new Member();
        member.username = username;
        member.password = password;
        member.provider = provider;
        member.email = email;
        member.phoneNumber = phoneNumber;
        member.address = address;
        return member;
    }

    public Member updateMember(String email, String password, String username, String phoneNumber, Address address) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.address = address;
        return this;
    }

    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }
}
