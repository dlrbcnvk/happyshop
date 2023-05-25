package com.project.happyshop.domain.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "MEMBER_ROLE")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
@Getter
public class MemberRole {

    @Id
    @GeneratedValue
    @Column(name = "member_role_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;
}
