package com.project.happyshop.entity;

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
}
