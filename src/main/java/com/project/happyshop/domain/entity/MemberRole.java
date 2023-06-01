package com.project.happyshop.domain.entity;

import lombok.*;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

@Entity
@Table(name = "MEMBER_ROLE")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
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

    // M:N -> 1:M & N:1 연관관계 편의 메서드
    @Transactional
    public void setMemberAndRole(Member member, Role role) {
        if (this.member != null) {
            this.member.getMemberRoles().remove(this);
        }
        this.member = member;
        member.getMemberRoles().add(this);

        if (this.role != null) {
            this.role.getMemberRoles().remove(this);
        }
        this.role = role;
        role.getMemberRoles().add(this);
    }
}
