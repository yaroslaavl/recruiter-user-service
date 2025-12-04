package org.yaroslaavl.userservice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.yaroslaavl.userservice.config.converter.KeyCloakAuthenticationRoleConverter;

import java.util.Collection;

@Slf4j
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
                                "/v3/api-docs/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/api/v1/auth/login",
                                "/api/v1/auth/refresh-token",
                                "/api/v1/auth/register-candidate",
                                "/api/v1/auth/register-recruiter",
                                "/api/v1/nip/verify",
                                "/api/v1/company/image/*",
                                "/api/v1/company/*",
                                "/api/v1/mail/request-verification-candidate",
                                "/api/v1/mail/request-verification-recruiter",
                                "/api/v1/user/exists",
                                "/api/v1/mail/verify-code").permitAll()
                        .requestMatchers(
                                "/api/v1/user/account",
                                "/api/v1/user/change-password",
                                "/api/v1/user/current",
                                "/api/v1/user/public-candidate",
                                "/api/v1/user/public-recruiter"
                        ).authenticated()
                        .requestMatchers(
                                "/api/v1/user/profile-data",
                                "/api/v1/user/candidate-info",
                                "/api/v1/user/me-candidate"
                        ).hasRole("VERIFIED_CANDIDATE")
                        .requestMatchers(
                                "/api/v1/user/recruiter-info",
                                "/api/v1/company/upload-image/*",
                                "/api/v1/company/info/*",
                                "/api/v1/user/me-recruiter"
                        ).hasRole("VERIFIED_RECRUITER")
                        .requestMatchers(
                                "/api/v1/recruiter-registration-request/*"
                        ).hasRole("MANAGER")
                        .requestMatchers(
                                "/api/v1/user/belongs",
                                "/api/v1/user/isApproved",
                                "/api/v1/user/filtered-candidates",
                                "/api/v1/user/short-info",
                                "/api/v1/user/batch-displayName").hasRole("INTERNAL_SERVICE")
        );

        return http.build();
    }
}
