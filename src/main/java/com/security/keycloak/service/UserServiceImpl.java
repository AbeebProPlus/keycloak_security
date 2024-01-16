package com.security.keycloak.service;

import com.security.keycloak.dto.AppUserDto;
import com.security.keycloak.dto.UpdateUserNameDto;
import com.security.keycloak.dto.UserDto;
import com.security.keycloak.model.User;
import com.security.keycloak.repo.UserRepo;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;

    public UserServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public User addUser(AppUserDto userDto) {
        User user = User.builder()
                .userName(userDto.getUserName())
                .email(userDto.getEmail())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .password(userDto.getPassword())
                .build();
        return userRepo.save(user);
    }

    @Override
    public User findByUserName(String name) {
        return userRepo.findByUserName(name);
    }

    @Override
    public User editUserName(UpdateUserNameDto updateUserNameDto) {
        User user = userRepo.findById(updateUserNameDto.getUserId())
                .orElseThrow(() -> new RuntimeException("USER NOT FOUND"));
        user.setUserName(updateUserNameDto.getNewName());
        return user;
    }
}
