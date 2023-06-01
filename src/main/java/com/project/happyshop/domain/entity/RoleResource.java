package com.project.happyshop.domain.entity;

import lombok.*;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

@Entity
@Table(name = "ROLE_RESOURCE")
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Data
public class RoleResource {

    @Id
    @GeneratedValue
    @Column(name = "role_resource_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resource_id")
    private Resource resource;

    // M:N -> 1:M & N:1 연관관계 편의 메서드
    @Transactional
    public void setRoleAndResource(Role role, Resource resource) {
        if (this.role != null) {
            this.role.getRoleResources().remove(this);
        }
        this.role = role;
        role.getRoleResources().add(this);

        if (this.resource != null) {
            this.resource.getRoleResources().remove(this);
        }
        this.resource = resource;
        resource.getRoleResources().add(this);
    }
}
