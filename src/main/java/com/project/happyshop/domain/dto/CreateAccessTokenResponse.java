package com.project.happyshop.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CreateAccessTokenResponse {
    private String accessToken;
}
