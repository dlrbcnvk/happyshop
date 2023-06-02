package com.project.happyshop.domain.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ItemDto {

    private String itemName;
    private Integer price;
    private Integer quantity;
    private String description;
    private String imageUrl;
}
