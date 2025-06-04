package org.yaroslaavl.userservice.dto.read;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.Value;

import java.time.LocalDateTime;

@Data
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "userType",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = RecruiterReadDto.class, name = "RECRUITER"),
        @JsonSubTypes.Type(value = CandidateReadDto.class, name = "CANDIDATE")
})
public class UserReadDto {

     private String email;

     private String firstName;

     private String lastName;

     private String userType;

     private String accountStatus;

     private LocalDateTime createdAt;
}
