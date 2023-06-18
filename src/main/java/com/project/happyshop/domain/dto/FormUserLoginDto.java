package com.project.happyshop.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FormUserLoginDto {
    private String email;
    private String password;
}
