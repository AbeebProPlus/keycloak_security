package com.security.keycloak.service;

import com.security.keycloak.dto.StudentDto;
import com.security.keycloak.model.Student;

public interface StudentService {
    Student addStudent(StudentDto studentDto);
    Student findByName(String name);
}
