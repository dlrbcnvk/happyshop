package com.project.happyshop.domain;

import lombok.Getter;
import lombok.ToString;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@ToString
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
