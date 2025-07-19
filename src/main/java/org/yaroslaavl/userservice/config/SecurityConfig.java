package org.yaroslaavl.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.yaroslaavl.userservice.config.converter.KeyCloakAuthenticationRoleConverter;

import java.util.Collection;

@Configuration
@EnableScheduling
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

                                "/api/v1/auth/login",
                                "/api/v1/auth/refresh-token",
                                "/api/v1/auth/register-candidate",
                                "/api/v1/auth/register-recruiter",

                                "/api/v1/nip/verify",

                                "/api/v1/company/image/*",
                                "/api/v1/company/*",
                                "/api/v1/mail/request-verification-candidate",
                                "/api/v1/mail/request-verification-recruiter",
                                "/api/v1/mail/verify-code").permitAll()
                        .requestMatchers(
                                "/api/v1/user/account",
                                "/api/v1/user/change-password"
                        ).authenticated()
                        .requestMatchers(
                                "/api/v1/user/profile-data",
                                "/api/v1/user/candidate-info"
                        ).hasRole("VERIFIED_CANDIDATE")
                        .requestMatchers(
                                "/api/v1/user/recruiter-info",
                                "/api/v1/company/upload-image/*",
                                "/api/v1/company/info/*"
                        ).hasRole("VERIFIED_RECRUITER")
                        .requestMatchers(
                                "/api/v1/recruiter-registration-request/*"
                        ).hasRole("MANAGER")
        );

        return http.build();
    }
}
