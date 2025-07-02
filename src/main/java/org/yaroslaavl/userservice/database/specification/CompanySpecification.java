package org.yaroslaavl.userservice.database.specification;

import org.springframework.data.jpa.domain.Specification;
import org.yaroslaavl.userservice.database.entity.Company;
import org.yaroslaavl.userservice.database.entity.enums.company.CompanyStatus;

public class CompanySpecification {

    public static Specification<Company> getByName(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            if (keyword.trim().length() <= 1) {
                return criteriaBuilder.disjunction();
            }

            query.orderBy(criteriaBuilder.asc(root.get("name")));
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), keyword.toLowerCase() + "%");
        };
    }

    public static Specification<Company> hasStatus(CompanyStatus companyStatus) {
        return (root, query, criteriaBuilder) -> {
            if (companyStatus == null || companyStatus.toString().isEmpty()) {
                return criteriaBuilder.disjunction();
            }

            return criteriaBuilder.equal(root.get("companyStatus"), companyStatus);
        };
    }
}
