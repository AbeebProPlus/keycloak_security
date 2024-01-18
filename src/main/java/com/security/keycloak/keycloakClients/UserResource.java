package com.security.keycloak.keycloakClients;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.security.keycloak.dto.Role;
import com.security.keycloak.dto.UserDto;
import com.security.keycloak.security.KeycloakSecurityUtil;
import com.security.keycloak.service.KeycloakService;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.common.util.CollectionUtil;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.ws.rs.core.Response;

@RestController
@RequestMapping("/keycloak")
@SecurityRequirement(name = "keycloak")
@RequiredArgsConstructor
public class UserResource {

    private final KeycloakSecurityUtil keycloakUtil;
    private final KeycloakService keycloakService;

    @Value("${realm}")
    private String realm;

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable("id") String id) {
        UserDto user = keycloakService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/user")
    public ResponseEntity<Void> createUser(@RequestBody UserDto user) {
        keycloakService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @GetMapping
    @RequestMapping("/users")
    public ResponseEntity<List<UserDto>> getUsers() {
        List<UserDto> users = keycloakService.getUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/user")
    public Response updateUser(UserDto user) {
        UserRepresentation userRep = mapUserRep(user);
        Keycloak keycloak = keycloakUtil.getKeycloakInstance();
        keycloak.realm(realm).users().get(user.getId()).update(userRep);
        return Response.ok(user).build();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") String id) {
        keycloakService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping( "/users/{id}/roles")
    public List<Role> getRoles(@PathVariable("id") String id) {
        Keycloak keycloak = keycloakUtil.getKeycloakInstance();
        return RoleResource.mapRoles(keycloak.realm(realm).users()
                .get(id).roles().realmLevel().listAll());
    }


    @PostMapping("/users/{id}/roles/{roleName}") //userId in keycloak
    public ResponseEntity<Void> createRoleForUser(@PathVariable String id, @PathVariable String roleName) {
        keycloakService.createRoleForUser(id, roleName);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/create-with-role")
    public ResponseEntity<Void> createUserWithRole(@RequestBody UserDto user) {
        keycloakService.createUserWithRole(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }



    private UserRepresentation mapUserRep(UserDto user) {
        UserRepresentation userRep = new UserRepresentation();
        userRep.setId(user.getId());
        userRep.setUsername(user.getUserName());
        userRep.setFirstName(user.getFirstName());
        userRep.setLastName(user.getLastName());
        userRep.setEmail(user.getEmail());
        userRep.setEnabled(true);
        userRep.setEmailVerified(true);
        List<CredentialRepresentation> creds = new ArrayList<>();
        CredentialRepresentation cred = new CredentialRepresentation();
        cred.setTemporary(false);
        cred.setValue(user.getPassword());
        creds.add(cred);
        userRep.setCredentials(creds);
        return userRep;
    }

}