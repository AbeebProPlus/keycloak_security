package com.security.keycloak.repo;

import com.security.keycloak.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepo extends JpaRepository<Student, Long> {
    Student findByName(String name);
}
