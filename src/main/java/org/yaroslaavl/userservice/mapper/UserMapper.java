package org.yaroslaavl.userservice.mapper;

import org.mapstruct.Mapper;
import org.yaroslaavl.userservice.database.entity.User;
import org.yaroslaavl.userservice.dto.response.UserResponseDto;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDto toDto(User user);
}
