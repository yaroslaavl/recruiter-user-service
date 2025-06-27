package org.yaroslaavl.userservice.database.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.yaroslaavl.userservice.database.entity.enums.language.LanguageLevel;
import org.yaroslaavl.userservice.database.entity.enums.language.Languages;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "language", schema = "user_data")
public class CandidateLanguage extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;

    @Column(name = "language", nullable = false)
    @Enumerated(EnumType.STRING)
    private Languages language;

    @Column(name = "language_level", nullable = false)
    @Enumerated(EnumType.STRING)
    private LanguageLevel languageLevel;
}
