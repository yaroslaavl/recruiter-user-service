package org.yaroslaavl.userservice.service;

import org.yaroslaavl.userservice.dto.read.RecruiterReadDto;
import org.yaroslaavl.userservice.dto.request.RecruiterPositionRequest;

import java.util.UUID;

public interface RecruiterService {

    RecruiterReadDto updateUserInfo(RecruiterPositionRequest inputDto);

    boolean isRecruiterBelongToCompany(String recruiterKeyId, UUID companyId);
}
