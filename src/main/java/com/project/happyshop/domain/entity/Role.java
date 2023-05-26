package com.project.happyshop.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ROLE")
@ToString(exclude = {"roleResources", "memberRoles"})
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
@Getter
public class Role implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "role_id")
    private Long id;

    @Column(name = "role_name")
    private String roleName;

    @Column(name = "role_desc")
    private String roleDesc;

    @OneToMany(mappedBy = "role")
    private Set<MemberRole> memberRoles = new HashSet<>();

    @OneToMany(mappedBy = "role")
    private Set<RoleResource> roleResources = new HashSet<>();

    public void addRoleResource(RoleResource roleResource) {
        roleResources.add(roleResource);
        roleResource.setRole(this);
    }
}