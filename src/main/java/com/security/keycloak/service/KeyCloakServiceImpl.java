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
            createRole(userId, "user");
        }
    }
    private void createRole(String id, String roleName) {
        Keycloak keycloak = keycloakUtil.getKeycloakInstance();
        RoleRepresentation role = keycloak.realm(realm).roles().get(roleName).toRepresentation();
        keycloak.realm(realm).users().get(id).roles().realmLevel().add(Arrays.asList(role));
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
