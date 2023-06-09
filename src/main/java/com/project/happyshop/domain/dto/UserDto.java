package com.project.happyshop.domain.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDto {

    private String email;
    private String password;
    private String username;

    private String phoneNumber1;
    private String phoneNumber2;
    private String phoneNumber3;

    private String juso;
    private String jusoDetail;
    private String zipcode;
}
