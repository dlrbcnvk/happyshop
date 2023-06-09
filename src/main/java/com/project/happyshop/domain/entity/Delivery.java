package com.project.happyshop.domain.entity;

import com.project.happyshop.domain.Address;
import com.project.happyshop.domain.DeliveryStatus;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Delivery {

    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery")
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;
}
