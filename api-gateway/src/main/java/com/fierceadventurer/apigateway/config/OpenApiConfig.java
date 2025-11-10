package com.fierceadventurer.apigateway.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.*;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(title = "Social Media API Gateway", version = "1.0"),
        security = {
                // This applies the security scheme to all endpoints
                @SecurityRequirement(name = "keycloak_oauth_scheme")
        }
)
@SecurityScheme(
        name = "keycloak_oauth_scheme", // This name must match the one in @SecurityRequirement
        type = SecuritySchemeType.OAUTH2,
        flows = @OAuthFlows(
                authorizationCode = @OAuthFlow(
                        // This is the URL to your Keycloak's login page
                        authorizationUrl = "http://localhost:8088/realms/user-service/protocol/openid-connect/auth",
                        // This is the URL your API will use to exchange the code for a token
                        tokenUrl = "http://localhost:8088/realms/user-service/protocol/openid-connect/token",
                        scopes = {
                                @OAuthScope(name = "openid", description = "OpenID Connect scope"),
                                @OAuthScope(name = "profile", description = "User profile scope"),
                                @OAuthScope(name = "email", description = "User email scope")
                        }
                )
        )
)
public class OpenApiConfig {
}
