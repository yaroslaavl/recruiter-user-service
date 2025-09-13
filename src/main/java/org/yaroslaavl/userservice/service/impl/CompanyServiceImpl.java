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
import org.yaroslaavl.userservice.dto.response.CompanyResponseDto;
import org.yaroslaavl.userservice.dto.request.CompanyInfoRequest;
import org.yaroslaavl.userservice.dto.request.ImageUploadDto;
import org.yaroslaavl.userservice.dto.response.list.CompanyShortDto;
import org.yaroslaavl.userservice.dto.response.list.PageShortDto;
import org.yaroslaavl.userservice.exception.*;
import org.yaroslaavl.userservice.feignClient.dto.CompanyPreviewFeignDto;
import org.yaroslaavl.userservice.feignClient.recruiting.RecruitingFeignClient;
import org.yaroslaavl.userservice.mapper.CompanyMapper;
import org.yaroslaavl.userservice.service.CompanyService;
import org.yaroslaavl.userservice.service.MinioService;
import org.yaroslaavl.userservice.service.SecurityContextService;

import java.text.Normalizer;

import java.util.*;
import java.util.stream.Collectors;

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
    private final RecruitingFeignClient recruitingFeignClient;

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

    /**
     * Creates a new company or retrieves an existing one based on the NIP (tax identification number).
     * If a company with the given NIP already exists in the database, it returns that company.
     * Otherwise, it creates a new company with the given details and persists it in the database.
     *
     * @param companyExecutedDto an object containing the details of the company
     *                           including name, NIP, city, post code, street, and voivodeship
     * @return the existing or newly created {@link Company} entity
     */
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

    /**
     * Handles the uploading of images for a specific company. The method supports
     * uploading multiple types of images (e.g., logo, banner) and updates the company's
     * details with the respective image URLs.
     *
     * @param imageUploadDto the data transfer object that contains the image files
     *                        to be uploaded and associated metadata
     * @param companyId the unique identifier of the company for which the images
     *                   are being uploaded
     * @throws ImageUploadException if an unexpected error occurs during the image upload process
     */
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

    /**
     * Retrieves the image URL based on the specified image type and company ID.
     *
     * @param imageType the type of image to retrieve, such as LOGO or BANNER
     * @param companyId the unique identifier of the company whose image is being retrieved
     * @return the URL of the requested image if available, otherwise null
     * @throws EntityNotFoundException if the company with the specified ID is not found
     */
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

    /**
     * Updates the details of a company based on the provided request and company ID.
     * This method allows updating employee count, description, and website URL of the company.
     *
     * @param companyInfoRequest the request object containing updated company information
     * @param companyId the unique identifier of the company to update
     */
    @Override
    @Transactional
    public void updateCompanyDetails(CompanyInfoRequest companyInfoRequest, UUID companyId) {
        Company company = checkCompanyDetails(companyId);

        Optional.ofNullable(companyInfoRequest.getEmployeeCount()).ifPresent(company::setEmployeeCount);
        Optional.ofNullable(companyInfoRequest.getDescription()).ifPresent(company::setDescription);

        validateCompanyWebsite(companyInfoRequest, company);

        companyRepository.save(company);
    }

    @Override
    public CompanyResponseDto getCompany(UUID companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Company not found"));

        return companyMapper.toDto(company);
    }

    /**
     * Searches for companies based on a keyword and returns a pageable result of company data transfer objects.
     *
     * @param keyword the search keyword used to filter companies by name
     * @param pageable the pagination and sorting information
     * @return a paginated list of CompanyReadDto objects that match the search criteria
     */
    @Override
    public PageShortDto<CompanyShortDto> getFilteredCompanies(String keyword, Pageable pageable) {
        log.info("Getting filtered companies with keyword: {}", keyword);

        Specification<Company> specification = Specification
                .where(CompanySpecification.getByName(keyword))
                .and(CompanySpecification.hasStatus(CompanyStatus.APPROVED));
        Page<Company> companies = companyRepository.findAll(specification, pageable);

        Map<UUID, Long> companyVacanciesCount = recruitingFeignClient.getCompanyVacanciesCount(
                companies.getContent()
                        .stream()
                        .map(Company::getId)
                        .collect(Collectors.toSet()));

        log.info("Found {} filtered companies", companies.getTotalElements());
        return new PageShortDto<>(
                companyMapper.toShortDto(companies.getContent(), companyVacanciesCount),
                companies.getTotalElements(),
                companies.getTotalPages(),
                companies.getNumber(),
                companies.getSize());
    }

    @Override
    public Company getCompanyById(UUID companyId) {
        return companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Company not found"));
    }

    @Override
    public Map<UUID, CompanyPreviewFeignDto> getPreviewInfo(Set<UUID> companyIds) {
        List<Company> companiesByCompanyIds = companyRepository.findCompaniesByCompanyIds(companyIds);

        return companiesByCompanyIds.stream().collect(Collectors.toMap(Company::getId, companyMapper::toPreviewFeignDto));
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

    private void validateCompanyWebsite(CompanyInfoRequest companyInfoRequest, Company company) {
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
    }

    /**
     * Normalizes the given object name by performing the following steps:
     * - Converts the input string into a normalized form to remove diacritical marks.
     * - Transforms all characters to lowercase.
     * - Removes all whitespace characters.
     * - Replaces specific characters based on a predefined mapping.
     *
     * @param objectName the input string representing the object name to be normalized.
     * @return the normalized string
     */
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
