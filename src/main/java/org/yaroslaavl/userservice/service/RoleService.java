package org.yaroslaavl.userservice.service;

import org.yaroslaavl.userservice.database.entity.enums.user.UserType;

public interface RoleService {

    void assignRoles(String userId, UserType userType);
}
