package org.yaroslaavl.userservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.yaroslaavl.userservice.database.entity.RecruiterRegistrationRequest;
import org.yaroslaavl.userservice.dto.read.RecruiterRegistrationRequestReadDto;
import org.yaroslaavl.userservice.service.RecruiterRegistrationService;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecruiterRegistrationServiceImpl implements RecruiterRegistrationService {

    @Override
    public RecruiterRegistrationRequestReadDto create(RecruiterRegistrationRequest recruiterRegistrationRequest) {
        return null;
    }
}
