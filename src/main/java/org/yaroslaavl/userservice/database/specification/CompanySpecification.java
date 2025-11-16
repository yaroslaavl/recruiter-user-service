package org.yaroslaavl.userservice.database.specification;

import org.springframework.data.jpa.domain.Specification;
import org.yaroslaavl.userservice.database.entity.Company;
import org.yaroslaavl.userservice.database.entity.enums.company.CompanyStatus;

import java.util.List;

public class CompanySpecification {

    public static Specification<Company> getByName(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            if (keyword.trim().length() <= 1) {
                return criteriaBuilder.disjunction();
            }

            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), keyword.toLowerCase() + "%");
        };
    }

    public static Specification<Company> hasStatus(List<CompanyStatus> companyStatuses) {
        return (root, query, criteriaBuilder) -> {
            if (companyStatuses == null || companyStatuses.isEmpty()) {
                return criteriaBuilder.disjunction();
            }

            return root.get("companyStatus").in(companyStatuses);
        };
    }
}
