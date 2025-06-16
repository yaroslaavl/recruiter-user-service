package org.yaroslaavl.userservice.database.entity;

import jakarta.persistence.*;
import lombok.*;
import org.yaroslaavl.userservice.database.entity.enums.profile.AvailableFrom;
import org.yaroslaavl.userservice.database.entity.enums.profile.Salary;
import org.yaroslaavl.userservice.database.entity.enums.profile.WorkMode;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "candidate_profile_data", schema = "user_data")
public class CandidateProfileData extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;

    @Enumerated(EnumType.STRING)
    @Column(name = "desiredSalary_pln", nullable = false)
    private Salary desiredSalary;

    @Enumerated(EnumType.STRING)
    @Column(name = "work_mode", nullable = false)
    private WorkMode workMode;

    @Enumerated(EnumType.STRING)
    @Column(name = "available_from", nullable = false)
    private AvailableFrom availableFrom;

    @Column(name = "available_hours_per_week", nullable = false)
    private Integer availableHoursPerWeek;
}
