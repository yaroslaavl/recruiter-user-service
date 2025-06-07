package org.yaroslaavl.userservice.service;

import org.yaroslaavl.userservice.dto.integrations.CompanyExecutedDto;
import org.yaroslaavl.userservice.dto.read.CompanyReadDto;

public interface CompanyService {

    CompanyReadDto create(CompanyExecutedDto companyExecutedDto);
}
