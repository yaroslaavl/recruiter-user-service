package org.yaroslaavl.userservice.service;

import org.yaroslaavl.userservice.database.entity.enums.profile.AvailableFrom;
import org.yaroslaavl.userservice.database.entity.enums.profile.Salary;
import org.yaroslaavl.userservice.database.entity.enums.profile.WorkMode;
import org.yaroslaavl.userservice.dto.response.*;
import org.yaroslaavl.userservice.dto.request.CandidateInfoRequest;
import org.yaroslaavl.userservice.dto.request.CandidateProfileDataRequest;
import org.yaroslaavl.userservice.feignClient.dto.UserFeignDto;

import java.util.Map;

public interface CandidateService {

    CandidateProfileDataResponseDto updateCandidateProfileData(CandidateProfileDataRequest candidateProfileDataRequest);

    CandidateResponseDto updateUserInfo(CandidateInfoRequest inputDto);

    Map<String, UserFeignDto> getFilteredCandidates(Salary salary, WorkMode workMode, Integer availableHoursPerWeek, AvailableFrom availableFrom);

    CandidatePrivateResponseDto getPersonalData();

    CandidatePublicResponseDto getPublicData(String candidateKeyId);
}
