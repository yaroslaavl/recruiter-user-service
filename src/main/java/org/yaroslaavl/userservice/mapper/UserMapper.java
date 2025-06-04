package org.yaroslaavl.userservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.yaroslaavl.userservice.database.entity.User;
import org.yaroslaavl.userservice.dto.read.UserReadDto;
import org.yaroslaavl.userservice.dto.registration.UserRegistrationDto;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "keycloakId", ignore = true)
    @Mapping(target = "userType", ignore = true)
    @Mapping(target = "accountStatus", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    User toEntity(UserRegistrationDto dto);

    UserReadDto toDto(User user);
}
