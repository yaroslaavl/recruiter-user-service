package org.yaroslaavl.userservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.yaroslaavl.userservice.database.entity.Candidate;
import org.yaroslaavl.userservice.database.entity.User;
import org.yaroslaavl.userservice.dto.response.CurrentUser;
import org.yaroslaavl.userservice.dto.response.UserResponseDto;
import org.yaroslaavl.userservice.feignClient.dto.UserShortDto;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDto toDto(User user);

    CurrentUser toCurrentUser(User user);

    @Mapping(target = "fullName", expression = "java(getCandidateFirstAndLastName(user))")
    UserShortDto toShortDto(User user);

    default String getCandidateFirstAndLastName(User user) {
        return user.getFirstName() + " " + user.getLastName();
    }
}
