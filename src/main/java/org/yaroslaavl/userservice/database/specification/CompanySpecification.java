package org.yaroslaavl.userservice.database.specification;

import jakarta.persistence.criteria.Expression;
import org.springframework.data.jpa.domain.Specification;
import org.yaroslaavl.userservice.database.entity.Company;
import org.yaroslaavl.userservice.database.entity.enums.company.CompanyStatus;

import java.util.List;

public class CompanySpecification {

    public static Specification<Company> getByName(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            String cleanedKeyword = keyword.toLowerCase().replaceAll("[^a-z0-9]", "");
            Expression<String> cleanedName = criteriaBuilder.function(
                    "regexp_replace",
                    String.class,
                    criteriaBuilder.lower(root.get("name")),
                    criteriaBuilder.literal("[^a-z0-9]"),
                    criteriaBuilder.literal("")
            );
            return criteriaBuilder.like(cleanedName, cleanedKeyword + "%");
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
