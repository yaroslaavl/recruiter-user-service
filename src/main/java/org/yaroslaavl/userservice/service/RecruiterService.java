package org.yaroslaavl.userservice.service;

import org.yaroslaavl.userservice.dto.read.RecruiterReadDto;
import org.yaroslaavl.userservice.dto.request.RecruiterPositionRequest;

public interface RecruiterService {

    RecruiterReadDto updateUserInfo(RecruiterPositionRequest inputDto);
}
