package org.yaroslaavl.userservice.service;

import org.yaroslaavl.userservice.dto.response.RecruiterPrivateResponseDto;
import org.yaroslaavl.userservice.dto.response.RecruiterPublicResponseDto;
import org.yaroslaavl.userservice.dto.response.RecruiterResponseDto;
import org.yaroslaavl.userservice.dto.request.RecruiterPositionRequest;

import java.util.UUID;

public interface RecruiterService {

    void updateUserInfo(RecruiterPositionRequest inputDto);

    boolean isRecruiterBelongToCompany(String recruiterKeyId, UUID companyId);

    RecruiterPrivateResponseDto getPersonalData();

    RecruiterPublicResponseDto getPublicData(String recruiterKeyId);
}
