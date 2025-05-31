package org.yaroslaavl.userservice.database.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "candidate", schema = "user_data")
public class Candidate extends User {

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "linkedin_link")
    private String linkedinLink;
}