package com.security.keycloak.service;

import com.security.keycloak.dto.StudentDto;
import com.security.keycloak.model.Student;
import com.security.keycloak.repo.StudentRepo;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepo studentRepo;

    public StudentServiceImpl(StudentRepo studentRepo) {
        this.studentRepo = studentRepo;
    }

    @Override
    public Student addStudent(StudentDto studentDto) {
        Student student = Student.builder()
                .name(studentDto.getName())
                .build();
        return studentRepo.save(student);
    }

    @Override
    public Student findByName(String name) {
        return studentRepo.findByName(name);
    }
}
