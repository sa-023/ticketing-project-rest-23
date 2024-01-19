package com.company.config;
import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
/*
 * 🚩 Keycloak Manual:
 * · We configured everything on keycloak, and we created our users manually in keycloak.
 *
 * 🦋 Implementing Security
 * · Once we have registered a client on the Keycloak server and set up individual user accounts with roles,
 *   we can use Spring Security and the Keycloak Spring Boot Adapter.
 * · To set up a Protected Resource:
 * 1. Add the Spring Security and Keycloak dependencies to the pom.xml file.
 * 2. Configure the service to point to our Keycloak server in the application.properties.
 * 3. Create the SecurityConfig.java class and define what and who can access the service.
 *    · To define access control rules, we need to create a class and extend it from KeyCloakWebSecurityConfigurerAdapter class.
 *      And override the following methods: · configure() · configureGlobal() · sessionAuthenticationStrategy() ·KeycloakConfigResolver()
 * ❗️ Also, we used SecurityContextHolder in the ProjectServiceImpl and TaskServiceImpl.java classes.
 *    The SecurityContextHolder is where Spring Security stores the details of who is authenticated. Spring Security does
 *    not care how the SecurityContextHolder is populated. If it contains a value, it is used as the currently authenticated user.
 *
 * 🖍️...
 * · @EnableWebSecurity: It allows Spring to find and automatically apply the class to the global WebSecurity.
 * · @EnableGlobalMethodSecurity(jsr250Enabled = true): The property jsr250Enabled=true enables support for the JSR-250 annotations.
 *   One of the most commonly used @RolesAllowed annotation comes under this. Hence, Spring will ignore these annotations unless you set the flag to true.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true)
public class SecurityConfig extends KeycloakWebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http.authorizeRequests()
                .anyRequest()
                .permitAll();
        http.csrf().disable();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
        auth.authenticationProvider(keycloakAuthenticationProvider);
    }

    @Bean
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }

    @Bean
    public KeycloakConfigResolver KeycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }





}