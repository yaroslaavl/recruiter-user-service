package org.yaroslaavl.userservice.service;

import org.yaroslaavl.userservice.database.entity.Company;
import org.yaroslaavl.userservice.database.entity.Recruiter;

public interface RecruiterRegistrationRequestService {

    Boolean create(Company company, Recruiter recruiter);

}
