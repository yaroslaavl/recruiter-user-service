package org.yaroslaavl.userservice.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeyCloakConfig {

    @Value("${keycloak.main_app.realm}")
    private String realm;

    @Value("${keycloak.main_app.client_id}")
    private String clientId;

    @Value("${keycloak.main_app.client_secret}")
    private String clientSecret;

    @Value("${keycloak.main_app.urls.auth}")
    private String authServerUrl;

    @Bean
    public Keycloak keycloak() {

        return KeycloakBuilder.builder()
                .realm(realm)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .serverUrl(authServerUrl)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .build();
    }
}
