package com.security.keycloak.controller;

import com.security.keycloak.dto.StudentDto;
import com.security.keycloak.service.StudentService;
import com.security.keycloak.model.Student;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('user')")
    public Student addStudent(@RequestBody StudentDto studentDto) {
        return studentService.addStudent(studentDto);
    }

    @GetMapping("/{name}")
    @PreAuthorize("hasRole('flyer')")
    public Student getStudentByName(@PathVariable String name) {
        return studentService.findByName(name);
    }

}