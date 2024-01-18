package com.security.keycloak.service;

import com.security.keycloak.dto.UserDto;
import jakarta.ws.rs.core.Response;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

public interface KeycloakService {
    void createUser(UserDto user);
    void createUserWithRole(UserDto userDto);
    void createRoleForUser(String userId, String roleName);

    List<UserDto> getUsers();
}
