package org.yaroslaavl.userservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.yaroslaavl.userservice.exception.JwtEmailNotFoundException;
import org.yaroslaavl.userservice.service.SecurityContextService;

@Slf4j
@Service
public class SecurityContextServiceImpl implements SecurityContextService {

    @Override
    public String getSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwt) {
            return jwt.getToken().getClaimAsString("email");
        }

        throw new JwtEmailNotFoundException("Email not found in JWT");
    }
}
