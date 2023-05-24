package com.project.happyshop.entity;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Address {

    private String juso;
    private String jusoDetail;
    private String zipcode;

    public Address(String juso, String jusoDetail, String zipcode) {
        this.juso = juso;
        this.jusoDetail = jusoDetail;
        this.zipcode = zipcode;
    }

    public Address() {

    }
}
