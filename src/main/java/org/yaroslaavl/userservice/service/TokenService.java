package org.yaroslaavl.userservice.service;

import org.yaroslaavl.userservice.dto.AuthTokenDto;
import org.yaroslaavl.userservice.dto.login.LoginDto;

public interface TokenService {

    AuthTokenDto login(LoginDto loginDto);

    AuthTokenDto refreshToken(String refreshToken);
}
