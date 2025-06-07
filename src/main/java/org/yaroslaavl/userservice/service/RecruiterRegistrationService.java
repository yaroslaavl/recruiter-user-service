package org.yaroslaavl.userservice.service;

import org.yaroslaavl.userservice.database.entity.RecruiterRegistrationRequest;
import org.yaroslaavl.userservice.dto.read.RecruiterRegistrationRequestReadDto;

public interface RecruiterRegistrationService {

    RecruiterRegistrationRequestReadDto create(RecruiterRegistrationRequest recruiterRegistrationRequest);
}
