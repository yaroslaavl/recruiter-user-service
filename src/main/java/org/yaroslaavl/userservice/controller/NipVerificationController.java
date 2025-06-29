package org.yaroslaavl.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.yaroslaavl.userservice.dto.integrations.CompanyExecutedDto;
import org.yaroslaavl.userservice.service.NipVerificationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/nip/")
public class NipVerificationController {

    private final NipVerificationService nipVerificationService;

    @PostMapping("/verify")
    public ResponseEntity<CompanyExecutedDto> nipVerification(@RequestParam("nip") String nip,
                                                              @RequestParam("email") String email) {
        CompanyExecutedDto companyExecutedDto = nipVerificationService.verification(nip, email);
        return ResponseEntity.ok(companyExecutedDto);
    }
}
