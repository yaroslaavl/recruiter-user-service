package org.yaroslaavl.userservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AuthTokenDto(String accessToken,
                           Long expiresIn,
                           String refreshToken,
                           Long refreshExpiresIn) implements Serializable {
}
