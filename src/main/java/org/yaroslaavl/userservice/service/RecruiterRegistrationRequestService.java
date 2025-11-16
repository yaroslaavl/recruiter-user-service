package org.yaroslaavl.userservice.service;

import org.springframework.data.domain.Pageable;
import org.yaroslaavl.userservice.database.entity.Company;
import org.yaroslaavl.userservice.database.entity.Recruiter;
import org.yaroslaavl.userservice.database.entity.enums.registrationRequest.RequestStatus;
import org.yaroslaavl.userservice.dto.response.RecruiterRegistrationRequestResponseDto;
import org.yaroslaavl.userservice.dto.response.list.PageShortDto;
import org.yaroslaavl.userservice.dto.response.list.RecruiterRegistrationRequestShortDto;

import java.time.LocalDate;
import java.util.UUID;

public interface RecruiterRegistrationRequestService {

    Boolean create(Company company, Recruiter recruiter);

    void confirmOrRejectRegistrationRequest(UUID registrationRequestId, RequestStatus requestStatus);

    PageShortDto<RecruiterRegistrationRequestShortDto> getFilteredRequests(RequestStatus status, LocalDate requestDateFrom, Pageable pageable);

    RecruiterRegistrationRequestResponseDto getRequestById(UUID registrationRequestId);
}
