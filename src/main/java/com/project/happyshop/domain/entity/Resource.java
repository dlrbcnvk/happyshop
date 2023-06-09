package com.project.happyshop.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "RESOURCE")
@ToString(exclude = {"roleResources"})
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Resource implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "resource_id")
    private Long id;

    @Column(name = "resource_name")
    private String resourceName;

    @Column(name = "http_method")
    private String httpMethod;

    @Column(name = "order_num")
    private int orderNum;

    @Column(name = "resource_type")
    private String resourceType;

    @OneToMany(mappedBy = "resource", fetch = FetchType.LAZY)
    private Set<RoleResource> roleResources = new HashSet<>();

    public void addRoleResource(RoleResource roleResource) {
        roleResources.add(roleResource);
        roleResource.setResource(this);
    }

    public void setResourceInfos(
            String resourceName,
            String httpMethod,
            int orderNum,
            String resourceType
    ) {
        this.resourceName = resourceName;
        this.httpMethod = httpMethod;
        this.orderNum = orderNum;
        this.resourceType = resourceType;
    }
}
