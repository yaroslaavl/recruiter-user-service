package org.yaroslaavl.userservice.service;

import org.yaroslaavl.userservice.dto.integrations.CompanyExecutedDto;

@FunctionalInterface
public interface NipVerificationService {

    CompanyExecutedDto verification(String nip, String email);
}
