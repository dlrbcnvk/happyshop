package com.project.happyshop.domain.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDto {

    private String username;
    private String password;
    private String email;

    private String phoneNumber1;
    private String phoneNumber2;
    private String phoneNumber3;

    private String juso;
    private String jusoDetail;
    private String zipcode;
}
