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
}
