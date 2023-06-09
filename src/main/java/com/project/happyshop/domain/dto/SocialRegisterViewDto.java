package com.project.happyshop.domain.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SocialRegisterViewDto {

    private String email;
    private String username;
}
