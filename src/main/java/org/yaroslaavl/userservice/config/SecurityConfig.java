package org.yaroslaavl.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.yaroslaavl.userservice.config.converter.KeyCloakAuthenticationRoleConverter;

import java.util.Collection;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwtConfigurer -> jwtConfigurer
                                .jwtAuthenticationConverter(jwtToken -> {
                                    Collection<GrantedAuthority> authorities = new KeyCloakAuthenticationRoleConverter().convert(jwtToken);
                                    return new JwtAuthenticationToken(jwtToken, authorities);
                                })
                        )
                );

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                request -> request
                        .requestMatchers(
                                "/error",
                                "/actuator/health",
                                "/api/v1/user/test-1",

                                "/api/v1/auth/login",
                                "/api/v1/auth/refresh-token",
                                "/api/v1/auth/register-candidate",
                                "/api/v1/auth/register-recruiter",

                                "/api/v1/nip/verify",

                                "/api/v1/mail/request-verification",
                                "/api/v1/mail/verify-code").permitAll()
                        .requestMatchers(
                                "/api/v1/user/test-2").hasRole("VERIFIED_CANDIDATE")
        );

        return http.build();
    }
}
