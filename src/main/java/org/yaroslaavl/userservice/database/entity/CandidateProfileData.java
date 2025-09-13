package org.yaroslaavl.userservice.database.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.yaroslaavl.userservice.database.entity.enums.profile.AvailableFrom;
import org.yaroslaavl.userservice.database.entity.enums.profile.Salary;
import org.yaroslaavl.userservice.database.entity.enums.profile.WorkMode;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "candidate_profile_data", schema = "user_data")
public class CandidateProfileData extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;

    @Enumerated(EnumType.STRING)
    @Column(name = "desiredsalary_pln", nullable = false)
    private Salary desiredSalary;

    @Enumerated(EnumType.STRING)
    @Column(name = "work_mode", nullable = false)
    private WorkMode workMode;

    @Enumerated(EnumType.STRING)
    @Column(name = "available_from", nullable = false)
    private AvailableFrom availableFrom;

    @Max(value = 40)
    @Min(value = 16)
    @Column(name = "available_hours_per_week", nullable = false)
    private Integer availableHoursPerWeek;
}
