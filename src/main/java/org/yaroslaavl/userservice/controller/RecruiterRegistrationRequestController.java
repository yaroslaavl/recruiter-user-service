package org.yaroslaavl.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yaroslaavl.userservice.database.entity.enums.registrationRequest.RequestStatus;
import org.yaroslaavl.userservice.service.RecruiterRegistrationRequestService;

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
}
