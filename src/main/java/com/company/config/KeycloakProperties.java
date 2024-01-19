package com.company.config;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
/*
 * 🖍️...
 * · @Value("${property.name}"): It is used to inject values from property files into our Java class attributes.
 *   It binds the value of a property to a field in a Spring component class.
 *   We annotate Java class fields with @Value and provide the property name as a parameter.
 */
@Component
@Getter
@Setter
public class KeycloakProperties {

    @Value("${keycloak.realm}")
    private String realm;
    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;
    @Value("${keycloak.resource}")
    private String clientId;
    @Value("${keycloak.credentials.secret}")
    private String clientSecret;
    @Value("${master.user}")
    private String masterUser;
    @Value("${master.user.password}")
    private String masterUserPswd;
    @Value("${master.realm}")
    private String masterRealm;
    @Value("${master.client}")
    private String masterClient;




}
