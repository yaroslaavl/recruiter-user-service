package org.yaroslaavl.userservice.dto.response;

import org.yaroslaavl.userservice.database.entity.enums.profile.AvailableFrom;
import org.yaroslaavl.userservice.database.entity.enums.profile.Salary;
import org.yaroslaavl.userservice.database.entity.enums.profile.WorkMode;

import java.util.List;
import java.util.UUID;

public record CandidateProfileDataResponseDto(UUID candidateId,
                                              Salary desiredSalary,
                                              WorkMode workMode,
                                              AvailableFrom availableFrom,
                                              Integer availableHoursPerWeek,
                                              List<LanguageResponseDto> languageResponseDtoList) {

}
