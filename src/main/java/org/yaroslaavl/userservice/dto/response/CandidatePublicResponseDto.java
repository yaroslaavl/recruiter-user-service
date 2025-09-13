package org.yaroslaavl.userservice.dto.response;

import jakarta.validation.constraints.NotBlank;

public record CandidatePublicResponseDto(
        @NotBlank String displayName,
        @NotBlank String phoneNumber,
        String linkedinLink,
        CandidateProfileDataResponseDto candidateProfileDataResponseDto
) { }
