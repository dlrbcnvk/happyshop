package com.project.happyshop.domain.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDto {

    @NotBlank
    @NotNull
    private String email;

    @NotBlank
    @NotNull
    private String password;

    @NotBlank
    @NotNull
    private String username;

    @NotBlank
    @NotNull
    private String phoneNumber1;
    @NotBlank
    @NotNull
    private String phoneNumber2;
    @NotBlank
    @NotNull
    private String phoneNumber3;

    @NotBlank
    @NotNull
    private String juso;
    @NotBlank
    @NotNull
    private String jusoDetail;
    @NotBlank
    @NotNull
    private String zipcode;
}
