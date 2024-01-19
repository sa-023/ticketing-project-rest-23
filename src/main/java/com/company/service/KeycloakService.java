package com.company.service;
import com.company.dto.UserDTO;
import javax.ws.rs.core.Response;

public interface KeycloakService {
    Response userCreate(UserDTO userDTO); // Response will save the user in keycloak.
    void delete(String userName);




}
