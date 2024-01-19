package com.company.service.impl;
import com.company.config.KeycloakProperties;
import com.company.dto.UserDTO;
import com.company.service.KeycloakService;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import javax.ws.rs.core.Response;
import java.util.List;
import static java.util.Arrays.asList;
import static org.keycloak.admin.client.CreatedResponseUtil.getCreatedId;
/*
 * 🖍️...
 * · Response class comes from jboss-jaxrs-api_2.1_spec dependency, and it will save the user in keycloak.
 * · CredentialRepresentation class: It comes from keycloak-admin-client dependency.
 *   It will capture the password from DTO (Request Body) and store it in a CredentialRepresentation object. (Password represent Keycloak "Set Password" field.)
 * · UserRepresentation class: It comes from keycloak-admin-client dependency.
 *   It will capture the user details from DTO (Request Body) and store them in a UserRepresentation object. (All user details represent Keycloak "Add User" fields.)
 */
@Service
public class KeycloakServiceImpl implements KeycloakService {
    private final KeycloakProperties keycloakProperties;
    public KeycloakServiceImpl(KeycloakProperties keycloakProperties) {
        this.keycloakProperties = keycloakProperties;
    }


    @Override
    public Response userCreate(UserDTO userDTO) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setTemporary(false); // It should keep the User Set Password "Temporary" field OFF on Keycloak when a password is created.
        credential.setValue(userDTO.getPassWord());

        UserRepresentation keycloakUser = new UserRepresentation();
        keycloakUser.setUsername(userDTO.getUserName());
        keycloakUser.setFirstName(userDTO.getFirstName());
        keycloakUser.setLastName(userDTO.getLastName());
        keycloakUser.setEmail(userDTO.getUserName());
        keycloakUser.setCredentials(asList(credential));
        keycloakUser.setEmailVerified(true);
        keycloakUser.setEnabled(true);

        Keycloak keycloak = getKeycloakInstance();
        RealmResource realmResource = keycloak.realm(keycloakProperties.getRealm());
        UsersResource usersResource = realmResource.users();
        Response result = usersResource.create(keycloakUser); // Create Keycloak user.

        String userId = getCreatedId(result);
        ClientRepresentation appClient = realmResource.clients()
                .findByClientId(keycloakProperties.getClientId()).get(0);

        RoleRepresentation userClientRole = realmResource.clients().get(appClient.getId()) // Creating the Role
                .roles().get(userDTO.getRole().getDescription()).toRepresentation();

        realmResource.users().get(userId).roles().clientLevel(appClient.getId())
                .add(List.of(userClientRole));

        keycloak.close();
        return result;
    }

    @Override
    public void delete(String userName) {
        Keycloak keycloak = getKeycloakInstance();
        RealmResource realmResource = keycloak.realm(keycloakProperties.getRealm());
        UsersResource usersResource = realmResource.users();

        List<UserRepresentation> userRepresentations = usersResource.search(userName);
        String uid = userRepresentations.get(0).getId();
        usersResource.delete(uid);
        keycloak.close();
    }

    private Keycloak getKeycloakInstance(){ // This method opens a new Keycloak instance to be used in the other impl methods.
        return Keycloak.getInstance(keycloakProperties.getAuthServerUrl(),
                keycloakProperties.getMasterRealm(), keycloakProperties.getMasterUser()
                , keycloakProperties.getMasterUserPswd(), keycloakProperties.getMasterClient());
    }






}