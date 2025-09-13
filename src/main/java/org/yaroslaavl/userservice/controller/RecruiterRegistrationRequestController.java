package org.yaroslaavl.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yaroslaavl.userservice.database.entity.enums.registrationRequest.RequestStatus;
import org.yaroslaavl.userservice.dto.response.RecruiterRegistrationRequestResponseDto;
import org.yaroslaavl.userservice.dto.response.list.PageShortDto;
import org.yaroslaavl.userservice.dto.response.list.RecruiterRegistrationRequestShortDto;
import org.yaroslaavl.userservice.service.RecruiterRegistrationRequestService;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recruiter-registration-request")
public class RecruiterRegistrationRequestController {

    private final RecruiterRegistrationRequestService recruiterRegistrationRequestService;

    @PatchMapping("/{registrationRequestId}")
    public ResponseEntity<String> updateRegistrationRequestStatus(@PathVariable("registrationRequestId") UUID registrationRequestId,
                                                                  @RequestParam("requestStatus") String requestStatus) {
        recruiterRegistrationRequestService.confirmOrRejectRegistrationRequest(registrationRequestId, RequestStatus.valueOf(requestStatus));

        String responseStatusInfo = "Status has changed to: %s".formatted(requestStatus);
        return ResponseEntity.ok(responseStatusInfo);
    }

    @GetMapping("/search")
    public ResponseEntity<PageShortDto<RecruiterRegistrationRequestShortDto>> getFilteredRequests(@RequestParam(required = false) RequestStatus status,
                                                                                                  @RequestParam(required = false) LocalDateTime requestDateFro,
                                                                                                  @PageableDefault(size = 15) Pageable pageable) {
        return ResponseEntity.ok(recruiterRegistrationRequestService.getFilteredRequests(status, requestDateFro, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecruiterRegistrationRequestResponseDto> getRequestById(@PathVariable("id") UUID registrationRequestId) {
        return ResponseEntity.ok(recruiterRegistrationRequestService.getRequestById(registrationRequestId));
    }
}
