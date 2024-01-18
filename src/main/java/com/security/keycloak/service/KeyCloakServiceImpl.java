package com.security.keycloak.service;

import com.security.keycloak.dto.UserDto;
import com.security.keycloak.security.KeycloakSecurityUtil;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KeyCloakServiceImpl implements KeycloakService{

    private final KeycloakSecurityUtil keycloakUtil;
    @Value("${realm}")
    private String realm;

    @Override
    public void createUser(UserDto user) {
        UserRepresentation userRep = mapUserRep(user);
        Keycloak keycloak = keycloakUtil.getKeycloakInstance();
        keycloak.realm(realm).users().create(userRep);
    }
    @Override
    public void createUserWithRole(UserDto user) {
        UserRepresentation userRep = mapUserRep(user);
        Keycloak keycloak = keycloakUtil.getKeycloakInstance();
        Response response = keycloak.realm(realm).users().create(userRep);

        if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
            URI location = response.getLocation();
            String userId = extractUserIdFromLocation(location);
            createRoleForUser(userId, "user");
        }
    }

    @Override
    public void createRoleForUser(String id, String roleName) {
        Keycloak keycloak = keycloakUtil.getKeycloakInstance();
        RoleRepresentation role = keycloak.realm(realm).roles().get(roleName).toRepresentation();
        keycloak.realm(realm).users().get(id).roles().realmLevel().add(Arrays.asList(role));
    }

    @Override
    public List<UserDto> getUsers() {
        Keycloak keycloak = keycloakUtil.getKeycloakInstance();
        List<UserRepresentation> userRepresentations = keycloak.realm(realm).users().list();
        return mapUsers(userRepresentations);
    }

    @Override
    public UserDto getUserById(String id) {
        Keycloak keycloak = keycloakUtil.getKeycloakInstance();
        UserRepresentation userRepresentation = keycloak.realm(realm).users().get(id).toRepresentation();
        return mapUser(userRepresentation);
    }
    private List<UserDto> mapUsers(List<UserRepresentation> userRepresentations) {
        return userRepresentations.stream()
                .map(this::mapUser)
                .collect(Collectors.toList());
    }
    private UserDto mapUser(UserRepresentation userRepresentation) {
        UserDto userDto = new UserDto();
        userDto.setId(userRepresentation.getId());
        userDto.setFirstName(userRepresentation.getFirstName());
        userDto.setLastName(userRepresentation.getLastName());
        userDto.setEmail(userRepresentation.getEmail());
        userDto.setUserName(userRepresentation.getUsername());
        return userDto;
    }


    private String extractUserIdFromLocation(URI location) {
        String path = location.getPath();
        String[] pathSegments = path.split("/");
        return pathSegments[pathSegments.length - 1];
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
        List<CredentialRepresentation> creds = getCredentialRepresentations(user);
        userRep.setCredentials(creds);
        return userRep;
    }

    private static List<CredentialRepresentation> getCredentialRepresentations(UserDto user) {
        List<CredentialRepresentation> creds = new ArrayList<>();
        CredentialRepresentation cred = new CredentialRepresentation();
        cred.setTemporary(false);
        cred.setValue(user.getPassword());
        creds.add(cred);
        return creds;
    }
}
