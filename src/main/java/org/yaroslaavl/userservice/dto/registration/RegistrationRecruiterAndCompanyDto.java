package org.yaroslaavl.userservice.dto.registration;


import lombok.Getter;
import lombok.Setter;
import org.yaroslaavl.userservice.dto.integrations.CompanyExecutedDto;

@Getter
@Setter
public class RegistrationRecruiterAndCompanyDto {

    private RecruiterRegistrationDto recruiter;

    private CompanyExecutedDto company;
}