package org.yaroslaavl.userservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.yaroslaavl.userservice.service.RoleService;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    @Value("${keycloak.realm}")
    private String realm;

    private final Keycloak keycloak;

    @Override
    public void assignRole(String userId, String roleName) {
        UsersResource usersResource = keycloak.realm(realm).users();
        UserResource userResource = usersResource.get(userId);
        RolesResource roleResource = getRoleResource();
        RoleRepresentation representation = roleResource.get(roleName).toRepresentation();

        userResource.roles().realmLevel().add(Collections.singletonList(representation));
    }

    private RolesResource getRoleResource() {
        return keycloak.realm(realm).roles();
    }
}
