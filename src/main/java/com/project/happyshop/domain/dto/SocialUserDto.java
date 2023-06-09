package com.project.happyshop.domain.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SocialUserDto {

    private String email;
    private String username;

    private String phoneNumber1;
    private String phoneNumber2;
    private String phoneNumber3;

    private String juso;
    private String jusoDetail;
    private String zipcode;
}
