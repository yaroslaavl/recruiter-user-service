package org.yaroslaavl.userservice.database.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.yaroslaavl.userservice.database.entity.enums.CompanyRole;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "recruiter", schema = "user_data")
public class Recruiter extends User {

    @ManyToOne
    @JoinColumn(name = "company_id", referencedColumnName = "id", nullable = false)
    private Company company;

    @Column(name = "position")
    private String position;

    @Column(name = "company_role", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private CompanyRole companyRole;
}