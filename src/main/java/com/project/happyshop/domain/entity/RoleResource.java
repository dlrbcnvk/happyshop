package com.project.happyshop.domain.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "ROLE_RESOURCE")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
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
}
