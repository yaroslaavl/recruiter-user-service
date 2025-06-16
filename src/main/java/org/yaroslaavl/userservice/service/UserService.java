package org.yaroslaavl.userservice.service;

import org.yaroslaavl.userservice.dto.user.ChangePasswordRequest;
import org.yaroslaavl.userservice.dto.user.DeleteAccountRequest;

public interface UserService {

    void deleteAccount(DeleteAccountRequest userDeleteDto);

    boolean updatePassword(ChangePasswordRequest resetPasswordDto);
}
