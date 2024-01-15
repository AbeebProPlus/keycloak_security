package com.security.keycloak.service;

import com.security.keycloak.dto.AppUserDto;
import com.security.keycloak.dto.UserDto;
import com.security.keycloak.model.User;

public interface UserService {
    User addUser(AppUserDto userDto);
    User findByUserName(String name);
}
