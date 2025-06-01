package org.yaroslaavl.userservice.database.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.yaroslaavl.userservice.database.entity.enums.CompanyStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "company", schema = "user_data")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "nip", nullable = false, unique = true)
    private String nip;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "voivodeship")
    private String voivodeship;

    @Column(name = "locality")
    private String locality;

    @Column(name = "employee_count")
    private Integer employeeCount;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "company_status", nullable = false)
    private CompanyStatus companyStatus;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}