package org.yaroslaavl.userservice.service;

import org.yaroslaavl.userservice.database.entity.Company;
import org.yaroslaavl.userservice.dto.integrations.CompanyExecutedDto;

public interface CompanyService {

    Company createOrGet(CompanyExecutedDto companyExecutedDto);
}
