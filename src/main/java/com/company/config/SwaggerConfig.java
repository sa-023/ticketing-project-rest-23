package com.company.config;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/*
 * 🦋 OpenAPI Specification integration with Swagger:
 * 0. In Keycloak → Client → Settings, enable the "Standard Flow Enabled" field. Input "Valid Redirect URIs" (http://localhost:8081/*) and "Web Origins" (http://localhost:8081).
 * 1. We can add the springdoc-openapi-ui dependency to the pom.xml file.
 * 2. Create a SwaggerConfig class and annotate it with @OpenAPIDefinition.
 *    @OpenAPIDefinition annotation used to populate OpenAPI Object fields info, tags, servers, security and externalDocs.
 * 3. Run the application and send a request to http://localhost:8081/swagger-ui/index.html on the browser, and we should see the swagger documentation.
 * 4. We can modify our swagger documentation in the Controller class.
 * · @Tag(): By default, the name for the group of endpoints is name-controller. We can customize it with the @Tag annotation.
 *   Ex: @Tag(name = "User",description = "User API")
 * · @Operation(summary = "Read all users", description = "List of All Users"): A summary adds a description of the endpoint, and a description adds extra details.
 * · @ApiResponse(): Adds a single value.
 * · @ApiResponses(value={@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "400") }): Adds multiple values.
 *
 * 📌 To test the endpoints, click on the Authorize button on the Swagger document and provide the client_id and client_secret,
 *    and then Try it out and execute each API endpoint.
 */
@Configuration
@OpenAPIDefinition
public class SwaggerConfig { // This is a ready configuration to implement swagger documentation.
    private static final String OAUTH_SCHEME_NAME = "oAuth";
    private static final String PROTOCOL_URL_FORMAT = "%s/realms/%s/protocol/openid-connect";
    private final KeycloakProperties keycloakProperties;
    public SwaggerConfig(KeycloakProperties keycloakProperties) {
        this.keycloakProperties = keycloakProperties;
    }


    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(getInfo())
                .components(new Components()
                        .addSecuritySchemes(OAUTH_SCHEME_NAME, createOAuthScheme()))
                .addSecurityItem(new SecurityRequirement().addList(OAUTH_SCHEME_NAME));
    }

    private Info getInfo() {
        return new Info()
                .title("Ticketing App Rest")
                .description("Api documentation")
                .version("v1.0");
    }

    private SecurityScheme createOAuthScheme() {
        OAuthFlows flows = createOAuthFlows();
        return new SecurityScheme()
                .type(SecurityScheme.Type.OAUTH2)
                .flows(flows);
    }

    private OAuthFlows createOAuthFlows() {
        OAuthFlow flow = createAuthorizationCodeFlow();
        return new OAuthFlows()
                .authorizationCode(flow);
    }

    private OAuthFlow createAuthorizationCodeFlow() {
        var protocolUrl = String.format(PROTOCOL_URL_FORMAT,
                keycloakProperties.getAuthServerUrl(), keycloakProperties.getRealm());
        return new OAuthFlow()
                .authorizationUrl(protocolUrl + "/auth")
                .tokenUrl(protocolUrl + "/token")
                .scopes(new Scopes().addString("openid", ""));
    }





}