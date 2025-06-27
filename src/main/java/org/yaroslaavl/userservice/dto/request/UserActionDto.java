package org.yaroslaavl.userservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.yaroslaavl.userservice.database.entity.User;

@Data
@AllArgsConstructor
public class UserActionDto {

    private String token;

    private User user;
}
