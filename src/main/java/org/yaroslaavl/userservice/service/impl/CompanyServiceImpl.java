package org.yaroslaavl.userservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yaroslaavl.userservice.database.entity.Company;
import org.yaroslaavl.userservice.database.entity.Recruiter;
import org.yaroslaavl.userservice.database.entity.enums.company.CompanyRole;
import org.yaroslaavl.userservice.database.entity.enums.company.CompanyStatus;
import org.yaroslaavl.userservice.database.entity.enums.company.ImageType;
import org.yaroslaavl.userservice.database.entity.enums.user.AccountStatus;
import org.yaroslaavl.userservice.database.repository.CompanyRepository;
import org.yaroslaavl.userservice.database.repository.RecruiterRepository;
import org.yaroslaavl.userservice.database.specification.CompanySpecification;
import org.yaroslaavl.userservice.dto.integrations.CompanyExecutedDto;
import org.yaroslaavl.userservice.dto.read.CompanyReadDto;
import org.yaroslaavl.userservice.dto.request.CompanyInfoRequest;
import org.yaroslaavl.userservice.dto.request.ImageUploadDto;
import org.yaroslaavl.userservice.exception.*;
import org.yaroslaavl.userservice.mapper.CompanyMapper;
import org.yaroslaavl.userservice.service.CompanyService;
import org.yaroslaavl.userservice.service.MinioService;
import org.yaroslaavl.userservice.service.SecurityContextService;

import java.text.Normalizer;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final MinioService minioService;
    private final SecurityContextService securityContextService;
    private final RecruiterRepository recruiterRepository;
    private final CompanyMapper companyMapper;

    private static final Map<Character, Character> REPLACEMENTS = Map.ofEntries(
            Map.entry('ł', 'l'),
            Map.entry('Ł', 'L'),
            Map.entry('ą', 'a'),
            Map.entry('Ą', 'A'),
            Map.entry('ć', 'c'),
            Map.entry('Ć', 'C'),
            Map.entry('ę', 'e'),
            Map.entry('Ę', 'E'),
            Map.entry('ń', 'n'),
            Map.entry('Ń', 'N'),
            Map.entry('ś', 's'),
            Map.entry('Ś', 'S'),
            Map.entry('ź', 'z'),
            Map.entry('Ź', 'Z'),
            Map.entry('ż', 'z'),
            Map.entry('Ż', 'Z')
    );

    @Override
    @Transactional
    public Company createOrGet(CompanyExecutedDto companyExecutedDto) {
        String nip = companyExecutedDto.getNip();

        Optional<Company> companyByNip =
                companyRepository.findCompanyByNip(nip);

        if (companyByNip.isEmpty()) {
            Company company = Company.builder()
                    .name(companyExecutedDto.getName())
                    .nip(nip)
                    .city(companyExecutedDto.getCity())
                    .postCode(companyExecutedDto.getPostCode())
                    .street(companyExecutedDto.getStreet())
                    .voivodeship(companyExecutedDto.getVoivodeship())
                    .companyStatus(CompanyStatus.PENDING)
                    .build();

            companyRepository.saveAndFlush(company);

            return company;
        }

        return companyByNip.get();
    }

    @Override
    @Transactional
    public void uploadImage(ImageUploadDto imageUploadDto, UUID companyId) {
        Company company = checkCompanyDetails(companyId);

        try {
            Map<ImageType, String> uploadedImage = minioService.upload(imageUploadDto, company.getId());
            for (Map.Entry<ImageType, String> entry: uploadedImage.entrySet()) {
                if (entry.getKey() == ImageType.LOGO) {
                    company.setLogoUrl(entry.getValue());
                } else if (entry.getKey() == ImageType.BANNER) {
                    company.setBannerUrl(entry.getValue());
                }
            }

            companyRepository.save(company);
        } catch (Exception e) {
            log.error("Unexpected error during image upload for company {}", companyId, e);
            throw new ImageUploadException("Unexpected error during image upload");
        }
    }

    @Override
    public String getImageObject(ImageType imageType, UUID companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Company not found"));

        String imageUrl = minioService.getObject(imageType, companyId);

        if (imageUrl != null && !imageUrl.isEmpty()) {
            if (ImageType.LOGO.equals(imageType)) {
                return company.getLogoUrl();
            } else if (ImageType.BANNER.equals(imageType)) {
                return company.getBannerUrl();
            }
        }

        return null;
    }

    @Override
    @Transactional
    public void updateCompanyDetails(CompanyInfoRequest companyInfoRequest, UUID companyId) {
        Company company = checkCompanyDetails(companyId);

        Optional.ofNullable(companyInfoRequest.getEmployeeCount()).ifPresent(company::setEmployeeCount);
        Optional.ofNullable(companyInfoRequest.getDescription()).ifPresent(company::setDescription);

        String websiteUrl = companyInfoRequest.getWebsiteUrl();
        if (websiteUrl != null && !websiteUrl.isEmpty()) {
            int indexOfCleanUrl = websiteUrl.indexOf("//");
            String urlWithoutProtocol = websiteUrl.substring(indexOfCleanUrl + 2).toLowerCase();

            int lastDot = urlWithoutProtocol.lastIndexOf(".");
            String baseDomain;
            if (lastDot != -1) {
                baseDomain = urlWithoutProtocol.substring(0, lastDot);
            } else {
                baseDomain = urlWithoutProtocol;
            }

            String normalizedString = normalize(company.getName());

            if (normalizedString.contains(baseDomain)) {
                company.setWebsite(websiteUrl);
            }
        }

        companyRepository.save(company);
    }

    @Override
    public CompanyReadDto getCompany(UUID companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Company not found"));

        return companyMapper.toDto(company);
    }

    @Override
    public Page<CompanyReadDto> search(String keyword, Pageable pageable) {
        Specification<Company> specification = Specification
                .where(CompanySpecification.getByName(keyword))
                .and(CompanySpecification.hasStatus(CompanyStatus.APPROVED));
        Page<Company> companies = companyRepository.findAll(specification, pageable);

        return companies.map(companyMapper::toDto);
    }

    @Override
    public Company getCompanyById(UUID companyId) {
        return companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Company not found"));
    }

    private Company checkCompanyDetails(UUID companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Company not found"));

        String recruiterDetails = securityContextService.getSecurityContext();

        Recruiter recruiter = recruiterRepository.findByEmail(recruiterDetails)
                .orElseThrow(() -> new EntityNotFoundException("Recruiter not found"));

        if (!recruiter.getCompany().getId().equals(company.getId())) {
            throw new InvalidCompanyAssociationException("Recruiter does not belong to the specified company");
        }

        if (recruiter.getAccountStatus() != AccountStatus.APPROVED || company.getCompanyStatus() != CompanyStatus.APPROVED) {
            throw new ApprovalRequiredException("Recruiter or company is not approved for this operation");
        }

        if (recruiter.getCompanyRole() != CompanyRole.ADMIN_RECRUITER) {
            throw new RecruiterNoUpdatePermission("Recruiter is not admin recruiter");
        }

        return company;
    }

    private String normalize(String objectName) {
        if (objectName == null) return null;

        String normalizedString = Normalizer.normalize(objectName, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .toLowerCase()
                .replaceAll("\\s+", "");

        StringBuilder cleanObjectName = new StringBuilder();

        for(char c : normalizedString.toCharArray()) {
            cleanObjectName.append(REPLACEMENTS.getOrDefault(c, c));
        }

        return cleanObjectName.toString();
    }
}
