package com.security.keycloak.controller;

import com.security.keycloak.dto.AppUserDto;
import com.security.keycloak.dto.UserDto;
import com.security.keycloak.service.UserService;
import com.security.keycloak.model.User;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@SecurityRequirement(name = "keycloak")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('user')")
    public User addUser(@RequestBody AppUserDto userDto) {
        return userService.addUser(userDto);
    }

    @GetMapping("/{name}")
    @PreAuthorize("hasRole('flyer')")
    public User getUserByUserName(@PathVariable String name) {
        return userService.findByUserName(name);
    }

}