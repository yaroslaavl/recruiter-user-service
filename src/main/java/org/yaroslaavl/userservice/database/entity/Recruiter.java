package org.yaroslaavl.userservice.database.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "recruiter", schema = "user_data")
public class Recruiter extends User {

    @ManyToOne
    @JoinColumn(name = "company_id", referencedColumnName = "id", nullable = false)
    private Company company;

    @Column(name = "position")
    private String position;
}