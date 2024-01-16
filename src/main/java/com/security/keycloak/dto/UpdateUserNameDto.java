package com.security.keycloak.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserNameDto {
    private Long userId;
    private String newName;
}
