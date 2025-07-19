package org.yaroslaavl.userservice.dto.read;

import org.yaroslaavl.userservice.database.entity.enums.profile.AvailableFrom;
import org.yaroslaavl.userservice.database.entity.enums.profile.Salary;
import org.yaroslaavl.userservice.database.entity.enums.profile.WorkMode;

import java.util.List;
import java.util.UUID;

public record CandidateProfileDataReadDto(UUID candidateId,
                                          Salary desiredSalary,
                                          WorkMode workMode,
                                          AvailableFrom availableFrom,
                                          Integer availableHoursPerWeek,
                                          List<LanguageReadDto> languageReadDtoList) {

}
