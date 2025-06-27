package org.yaroslaavl.userservice.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class CandidateProfileDataRequest {

    private String desiredSalary;

    private String workMode;

    private String availableFrom;

    private Integer availableHoursPerWeek;

    private List<LanguageRequest> languages;
}
