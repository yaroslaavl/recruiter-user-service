package org.yaroslaavl.userservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yaroslaavl.userservice.database.entity.Company;
import org.yaroslaavl.userservice.database.entity.enums.company.CompanyStatus;
import org.yaroslaavl.userservice.database.repository.CompanyRepository;
import org.yaroslaavl.userservice.dto.integrations.CompanyExecutedDto;
import org.yaroslaavl.userservice.service.CompanyService;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

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
}
