package org.yaroslaavl.userservice.service;

import org.springframework.data.domain.Pageable;
import org.yaroslaavl.userservice.database.entity.Company;
import org.yaroslaavl.userservice.database.entity.enums.company.ImageType;
import org.yaroslaavl.userservice.dto.integrations.CompanyExecutedDto;
import org.yaroslaavl.userservice.dto.response.CompanyResponseDto;
import org.yaroslaavl.userservice.dto.request.CompanyInfoRequest;
import org.yaroslaavl.userservice.dto.request.ImageUploadDto;
import org.yaroslaavl.userservice.dto.response.list.CompanyShortDto;
import org.yaroslaavl.userservice.dto.response.list.PageShortDto;
import org.yaroslaavl.userservice.feignClient.dto.CompanyPreviewFeignDto;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface CompanyService {

    Company createOrGet(CompanyExecutedDto companyExecutedDto);

    void uploadImage(ImageUploadDto imageUploadDto, UUID companyId);

    String getImageObject(ImageType imageType, UUID companyId);

    void updateCompanyDetails(CompanyInfoRequest companyInfoRequest, UUID companyId);

    CompanyResponseDto getCompany(UUID companyId);

    PageShortDto<CompanyShortDto> getFilteredCompanies(String keyword, Pageable pageable);

    Company getCompanyById(UUID companyId);

    Map<UUID, CompanyPreviewFeignDto> getPreviewInfo(Set<UUID> companyIds);
}
