package org.yaroslaavl.userservice.service;

import org.yaroslaavl.userservice.dto.request.*;

import java.util.Map;
import java.util.Set;

public interface UserService {

    void deleteAccount(DeleteAccountRequest userDeleteDto);

    boolean updatePassword(ChangePasswordRequest resetPasswordDto);

    boolean existsAccount(String email);

    boolean isAccountApproved(String userId);

    Map<String, String> usersDisplayName(Set<String> userIds, String currentUserEmail);


}
