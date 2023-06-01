package com.project.happyshop.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@ToString(exclude = {"seller"})
public class Item implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private Integer price;
    private Integer quantity;
    private String description;
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member seller;

    // 연관관계 편의 메소드
    public void setSeller(Member member) {
        // 기존 판매자와 관계를 제거
        if (this.seller != null) {
            this.seller.getItems().remove(this);
        }
        this.seller = member;
        member.getItems().add(this);
    }

    // 생성 메소드
    public static Item createItem(
            String name,
            Integer price,
            Integer quantity,
            String description,
            String imageUrl
    ) {
        Item item = new Item();
        item.name = name;
        item.price = price;
        item.quantity = quantity;
        item.description = description;
        item.imageUrl = imageUrl;
        return item;
    }

    // 기본 생성자
    public Item() { }
}
