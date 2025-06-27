package org.yaroslaavl.userservice.mapper;

import org.mapstruct.Mapper;
import org.yaroslaavl.userservice.database.entity.User;
import org.yaroslaavl.userservice.dto.read.UserReadDto;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserReadDto toDto(User user);
}
