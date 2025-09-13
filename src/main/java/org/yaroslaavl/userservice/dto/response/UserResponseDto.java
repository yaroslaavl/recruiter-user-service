package org.yaroslaavl.userservice.dto.response;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import org.yaroslaavl.userservice.database.entity.enums.user.AccountStatus;

import java.time.LocalDateTime;

@Data
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "userType",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = RecruiterResponseDto.class, name = "RECRUITER"),
        @JsonSubTypes.Type(value = CandidateResponseDto.class, name = "CANDIDATE")
})
public class UserResponseDto {

     private String email;

     private String firstName;

     private String lastName;

     private AccountStatus accountStatus;

     private LocalDateTime createdAt;
}
