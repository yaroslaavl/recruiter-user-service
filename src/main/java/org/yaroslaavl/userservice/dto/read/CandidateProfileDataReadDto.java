package org.yaroslaavl.userservice.dto.read;

import java.util.List;
import java.util.UUID;

public record CandidateProfileDataReadDto(UUID candidateId,
                                          String desiredSalary,
                                          String workMode,
                                          String availableFrom,
                                          Integer availableHoursPerWeek,
                                          List<LanguageReadDto> languageReadDtoList) {

}
