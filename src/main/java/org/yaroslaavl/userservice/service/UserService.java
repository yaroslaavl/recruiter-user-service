package org.yaroslaavl.userservice.service;

import org.yaroslaavl.userservice.dto.request.*;

public interface UserService {

    void deleteAccount(DeleteAccountRequest userDeleteDto);

    boolean updatePassword(ChangePasswordRequest resetPasswordDto);

    boolean existsAccount(String email);
}
