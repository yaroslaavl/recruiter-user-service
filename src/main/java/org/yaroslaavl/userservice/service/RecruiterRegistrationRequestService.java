package org.yaroslaavl.userservice.service;

import org.yaroslaavl.userservice.database.entity.Company;
import org.yaroslaavl.userservice.database.entity.Recruiter;
import org.yaroslaavl.userservice.database.entity.enums.registrationRequest.RequestStatus;
import org.yaroslaavl.userservice.dto.request.RecruiterRegistrationRequestUpdateStatus;

import java.util.UUID;

public interface RecruiterRegistrationRequestService {

    Boolean create(Company company, Recruiter recruiter);

    void confirmOrRejectRegistrationRequest(UUID registrationRequestId, RequestStatus requestStatus);
}
