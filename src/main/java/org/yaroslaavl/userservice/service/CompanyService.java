package org.yaroslaavl.userservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.yaroslaavl.userservice.database.entity.Company;
import org.yaroslaavl.userservice.database.entity.enums.company.ImageType;
import org.yaroslaavl.userservice.dto.integrations.CompanyExecutedDto;
import org.yaroslaavl.userservice.dto.read.CompanyReadDto;
import org.yaroslaavl.userservice.dto.request.CompanyInfoRequest;
import org.yaroslaavl.userservice.dto.request.ImageUploadDto;

import java.util.UUID;

public interface CompanyService {

    Company createOrGet(CompanyExecutedDto companyExecutedDto);

    void uploadImage(ImageUploadDto imageUploadDto, UUID companyId);

    String getImageObject(ImageType imageType, UUID companyId);

    void updateCompanyDetails(CompanyInfoRequest companyInfoRequest, UUID companyId);

    CompanyReadDto getCompany(UUID companyId);

    Page<CompanyReadDto> search(String keyword, Pageable pageable);
}
