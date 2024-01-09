package com.security.keycloak.service;

import com.security.keycloak.dto.UserDto;
import com.security.keycloak.model.User;

public interface UserService {
    User addUser(UserDto userDto);
    User findByUserName(String name);
}
