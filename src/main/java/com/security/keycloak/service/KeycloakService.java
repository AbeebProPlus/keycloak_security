package com.security.keycloak.service;

import com.security.keycloak.dto.UserDto;
import jakarta.ws.rs.core.Response;
import org.keycloak.representations.idm.UserRepresentation;

public interface KeycloakService {
    void createUser(UserDto user);
    void createUserWithRole(UserDto userDto);
    public void createRole(String id, String roleName);
}
