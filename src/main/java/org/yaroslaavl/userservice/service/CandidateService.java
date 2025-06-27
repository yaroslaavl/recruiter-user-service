package org.yaroslaavl.userservice.service;

import org.yaroslaavl.userservice.dto.read.CandidateProfileDataReadDto;
import org.yaroslaavl.userservice.dto.read.CandidateReadDto;
import org.yaroslaavl.userservice.dto.request.CandidateInfoRequest;
import org.yaroslaavl.userservice.dto.request.CandidateProfileDataRequest;

public interface CandidateService {

    CandidateProfileDataReadDto updateCandidateProfileData(CandidateProfileDataRequest candidateProfileDataRequest);

    CandidateReadDto updateUserInfo(CandidateInfoRequest inputDto);
}
