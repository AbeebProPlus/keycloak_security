package com.security.keycloak.model;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.GenerationType.AUTO;


@Data
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "AppUser")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
