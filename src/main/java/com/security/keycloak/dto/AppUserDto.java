package com.security.keycloak.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppUserDto {
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
