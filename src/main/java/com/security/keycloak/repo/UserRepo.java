package com.security.keycloak.repo;

import com.security.keycloak.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUserName(String name);
}
