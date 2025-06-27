package org.yaroslaavl.userservice.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yaroslaavl.userservice.database.entity.Candidate;
import org.yaroslaavl.userservice.database.entity.CandidateLanguage;
import org.yaroslaavl.userservice.database.entity.CandidateProfileData;
import org.yaroslaavl.userservice.database.entity.enums.language.LanguageLevel;
import org.yaroslaavl.userservice.database.entity.enums.language.Languages;
import org.yaroslaavl.userservice.database.entity.enums.profile.AvailableFrom;
import org.yaroslaavl.userservice.database.entity.enums.profile.Salary;
import org.yaroslaavl.userservice.database.entity.enums.profile.WorkMode;
import org.yaroslaavl.userservice.database.entity.enums.user.AccountStatus;
import org.yaroslaavl.userservice.database.repository.CandidateProfileDataRepository;
import org.yaroslaavl.userservice.database.repository.CandidateRepository;
import org.yaroslaavl.userservice.database.repository.LanguageRepository;
import org.yaroslaavl.userservice.database.repository.UserRepository;
import org.yaroslaavl.userservice.dto.read.CandidateProfileDataReadDto;
import org.yaroslaavl.userservice.dto.read.CandidateReadDto;
import org.yaroslaavl.userservice.dto.request.CandidateInfoRequest;
import org.yaroslaavl.userservice.dto.request.CandidateProfileDataRequest;
import org.yaroslaavl.userservice.dto.request.LanguageRequest;
import org.yaroslaavl.userservice.exception.LanguagePreviousDataDeletionException;
import org.yaroslaavl.userservice.exception.UserNotFoundException;
import org.yaroslaavl.userservice.mapper.CandidateMapper;
import org.yaroslaavl.userservice.mapper.LanguageMapper;
import org.yaroslaavl.userservice.service.CandidateService;
import org.yaroslaavl.userservice.service.SecurityContextService;
import org.yaroslaavl.userservice.service.UserInfoUpdate;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CandidateServiceImpl extends UserInfoUpdate<Candidate, CandidateInfoRequest, CandidateReadDto, CandidateMapper> implements CandidateService {

    private final LanguageRepository languageRepository;
    private final CandidateProfileDataRepository candidateProfileDataRepository;
    private final LanguageMapper languageMapper;
    private final CandidateRepository candidateRepository;
    private final CandidateMapper candidateMapper;

    public CandidateServiceImpl(SecurityContextService securityContextService,
                                UserRepository userRepository,
                                LanguageRepository languageRepository,
                                CandidateProfileDataRepository candidateProfileDataRepository,
                                LanguageMapper languageMapper, CandidateRepository candidateRepository, CandidateMapper candidateMapper) {
        super(securityContextService, userRepository);
        this.languageRepository = languageRepository;
        this.candidateProfileDataRepository = candidateProfileDataRepository;
        this.languageMapper = languageMapper;
        this.candidateRepository = candidateRepository;
        this.candidateMapper = candidateMapper;
    }

    @Override
    @Transactional
    public CandidateReadDto updateUserInfo(CandidateInfoRequest inputDto) {
        return super.updateUserInfo(inputDto, candidateMapper);
    }

    @Override
    @Transactional
    public CandidateProfileDataReadDto updateCandidateProfileData(CandidateProfileDataRequest candidateProfileDataRequest) {
        String userEmail = securityContextService.getSecurityContext();

        Candidate candidate = (Candidate) userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("Candidate not found"));

        List<CandidateLanguage> languageList = getLanguages(candidateProfileDataRequest, candidate);
        languageRepository.saveAllAndFlush(languageList);

        try {
            Set<UUID> idsToKeep = languageList.stream()
                    .map(CandidateLanguage::getId)
                    .collect(Collectors.toSet());

            if (!idsToKeep.isEmpty()) {
                languageRepository.deleteAllWhereIdsIsNotLike(idsToKeep);
            }

        } catch (Exception e) {
            throw new LanguagePreviousDataDeletionException("Error during deleting previous language data");
        }

        Optional<CandidateProfileData> candidateProfileDataByCandidateId =
                candidateProfileDataRepository.findCandidateProfileDataByCandidateId(candidate.getId());

        if (candidateProfileDataByCandidateId.isPresent()) {
            Optional.ofNullable(candidateProfileDataRequest.getDesiredSalary())
                    .map(Salary::valueOf)
                    .ifPresent(candidateProfileDataByCandidateId.get()::setDesiredSalary);

            Optional.ofNullable(candidateProfileDataRequest.getAvailableFrom())
                    .map(AvailableFrom::valueOf)
                    .ifPresent(candidateProfileDataByCandidateId.get()::setAvailableFrom);

            Optional.ofNullable(candidateProfileDataRequest.getAvailableHoursPerWeek())
                    .ifPresent(candidateProfileDataByCandidateId.get()::setAvailableHoursPerWeek);

            Optional.ofNullable(candidateProfileDataRequest.getWorkMode())
                    .map(WorkMode::valueOf)
                    .ifPresent(candidateProfileDataByCandidateId.get()::setWorkMode);

            CandidateProfileData profileData = candidateProfileDataRepository.save(candidateProfileDataByCandidateId.get());

            return toDto(profileData, languageList);
        } else {
            CandidateProfileData profileData = CandidateProfileData.builder()
                    .candidate(candidate)
                    .desiredSalary(Salary.valueOf(candidateProfileDataRequest.getDesiredSalary()))
                    .availableFrom(AvailableFrom.valueOf(candidateProfileDataRequest.getAvailableFrom()))
                    .availableHoursPerWeek(candidateProfileDataRequest.getAvailableHoursPerWeek())
                    .workMode(WorkMode.valueOf(candidateProfileDataRequest.getWorkMode()))
                    .build();

            candidate.setAccountStatus(AccountStatus.PROFILE_COMPLETE);
            candidateRepository.save(candidate);

            CandidateProfileData savedProfile = candidateProfileDataRepository.save(profileData);

            return toDto(savedProfile, languageList);
        }
    }

    private CandidateProfileDataReadDto toDto(CandidateProfileData profileData, List<CandidateLanguage> languages) {
        return new CandidateProfileDataReadDto(
                profileData.getCandidate().getId(),
                profileData.getDesiredSalary().toString(),
                profileData.getWorkMode().toString(),
                profileData.getAvailableFrom().toString(),
                profileData.getAvailableHoursPerWeek(),
                languageMapper.toEntity(languages)
        );
    }

    private List<CandidateLanguage> getLanguages(CandidateProfileDataRequest candidateProfileDataRequest, Candidate candidate) {
        List<LanguageRequest> languages = candidateProfileDataRequest.getLanguages();

        List<CandidateLanguage> languageList = new ArrayList<>();

        languages.forEach(languageRequest -> {
            CandidateLanguage language = CandidateLanguage.builder()
                    .candidate(candidate)
                    .language(Languages.valueOf(languageRequest.getLanguage()))
                    .languageLevel(LanguageLevel.valueOf(languageRequest.getLanguageLevel()))
                    .build();

            languageList.add(language);
        });
        return languageList;
    }
}
