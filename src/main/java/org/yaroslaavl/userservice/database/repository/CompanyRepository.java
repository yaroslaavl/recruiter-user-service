package org.yaroslaavl.userservice.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.yaroslaavl.userservice.database.entity.Company;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompanyRepository extends JpaRepository<Company, UUID>, JpaSpecificationExecutor<Company> {

    Optional<Company> findCompanyByNip(String nip);

    Optional<Company> findCompanyByIdAndRecruiterListIsEmpty(UUID companyId);
}
