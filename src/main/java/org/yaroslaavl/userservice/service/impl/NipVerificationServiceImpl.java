package org.yaroslaavl.userservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.yaroslaavl.userservice.dto.integrations.CompanyExecutedDto;
import org.yaroslaavl.userservice.service.NipVerificationService;

@Slf4j
@Service
@RequiredArgsConstructor
public class NipVerificationServiceImpl implements NipVerificationService {

    @Override
    public CompanyExecutedDto verification(String nip) {
        return null;
    }
}
