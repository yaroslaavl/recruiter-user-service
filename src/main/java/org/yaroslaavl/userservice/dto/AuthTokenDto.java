package org.yaroslaavl.userservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AuthTokenDto(@JsonProperty("access_token") String accessToken,
                           @JsonProperty("expires_in") Long expiresIn,
                           @JsonProperty("refresh_token") String refreshToken,
                           @JsonProperty("refresh_expires_in") Long refreshExpiresIn) implements Serializable {
}
