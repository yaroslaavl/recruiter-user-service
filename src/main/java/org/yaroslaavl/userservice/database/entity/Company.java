package org.yaroslaavl.userservice.database.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.yaroslaavl.userservice.database.entity.enums.company.CompanyStatus;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Table(name = "company", schema = "user_data")
public class Company extends BaseEntity {

    @Column(name = "nip", nullable = false, unique = true)
    private String nip;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "voivodeship", nullable = false)
    private String voivodeship;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "post_code", nullable = false)
    private String postCode;

    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "employee_count")
    private Integer employeeCount;

    @Column(name = "description")
    private String description;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "banner_url")
    private String bannerUrl;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
    private List<Recruiter> recruiterList;

    @Enumerated(EnumType.STRING)
    @Column(name = "company_status", nullable = false)
    private CompanyStatus companyStatus;
}