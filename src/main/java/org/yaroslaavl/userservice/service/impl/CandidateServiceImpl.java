package org.yaroslaavl.userservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yaroslaavl.userservice.database.entity.Candidate;
import org.yaroslaavl.userservice.database.entity.CandidateLanguage;
import org.yaroslaavl.userservice.database.entity.CandidateProfileData;
import org.yaroslaavl.userservice.database.entity.User;
import org.yaroslaavl.userservice.database.entity.enums.language.LanguageLevel;
import org.yaroslaavl.userservice.database.entity.enums.language.Languages;
import org.yaroslaavl.userservice.database.entity.enums.profile.AvailableFrom;
import org.yaroslaavl.userservice.database.entity.enums.profile.Salary;
import org.yaroslaavl.userservice.database.entity.enums.profile.WorkMode;
import org.yaroslaavl.userservice.database.entity.enums.user.AccountStatus;
import org.yaroslaavl.userservice.database.entity.enums.user.UserType;
import org.yaroslaavl.userservice.database.repository.CandidateProfileDataRepository;
import org.yaroslaavl.userservice.database.repository.CandidateRepository;
import org.yaroslaavl.userservice.database.repository.LanguageRepository;
import org.yaroslaavl.userservice.database.repository.UserRepository;
import org.yaroslaavl.userservice.dto.response.*;
import org.yaroslaavl.userservice.dto.request.CandidateInfoRequest;
import org.yaroslaavl.userservice.dto.request.CandidateProfileDataRequest;
import org.yaroslaavl.userservice.dto.request.LanguageRequest;
import org.yaroslaavl.userservice.exception.AccessInfoDeniedException;
import org.yaroslaavl.userservice.exception.LanguagePreviousDataDeletionException;
import org.yaroslaavl.userservice.exception.EntityNotFoundException;
import org.yaroslaavl.userservice.feignClient.dto.UserFeignDto;
import org.yaroslaavl.userservice.mapper.CandidateMapper;
import org.yaroslaavl.userservice.mapper.LanguageMapper;
import org.yaroslaavl.userservice.service.CandidateService;
import org.yaroslaavl.userservice.service.SecurityContextService;
import org.yaroslaavl.userservice.service.UserInfoUpdate;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
public class CandidateServiceImpl extends UserInfoUpdate<
        Candidate,
        CandidateInfoRequest,
        CandidateResponseDto,
        CandidateMapper> implements CandidateService {

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
    public void updateUserInfo(CandidateInfoRequest inputDto) {
        super.updateUserInfo(inputDto, candidateMapper);
    }

    /**
     * Updates the profile data of the authenticated candidate user.
     * This includes details such as desired salary, availability, work mode,
     * available hours per week, and language proficiency.
     *
     * @param candidateProfileDataRequest the request object containing updated candidate profile data,
     *                                    including desired salary, availability, work mode, available hours per week,
     *                                    and a list of languages with proficiency levels.
     **/
    @Override
    @Transactional
    public void updateCandidateProfileData(CandidateProfileDataRequest candidateProfileDataRequest) {
        String userEmail = securityContextService.getSecurityContext();

        Candidate candidate = (Candidate) userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("Candidate not found"));

        List<CandidateLanguage> languageList = getLanguages(candidateProfileDataRequest, candidate);
        languageRepository.saveAllAndFlush(languageList);

        try {
            Set<UUID> idsToKeep = languageList.stream()
                    .map(CandidateLanguage::getId)
                    .collect(Collectors.toSet());

            if (!idsToKeep.isEmpty()) {
                languageRepository.deleteAllUserLanguagesWhereIdsIsNotLike(idsToKeep, userEmail);
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

            candidateProfileDataRepository.save(candidateProfileDataByCandidateId.get());
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

            candidateProfileDataRepository.save(profileData);
        }
    }

    @Override
    public Map<String, UserFeignDto> getFilteredCandidates(Salary salary, WorkMode workMode, Integer availableHoursPerWeek, AvailableFrom availableFrom) {
        log.info("Filtering candidates with salary: {}, work mode: {}, available hours per week: {}, available from: {}", salary, workMode, availableHoursPerWeek, availableFrom);

        List<Candidate> filteredCandidates = candidateRepository.getFilteredCandidates(salary, workMode, availableHoursPerWeek, availableFrom);
        return filteredCandidates.stream().collect(Collectors.toMap(Candidate::getKeycloakId, candidateMapper::toFeignDto));
    }

    @Override
    public CandidatePrivateResponseDto getPersonalData() {
        String email = securityContextService.getSecurityContext();

        if (email != null && !email.isEmpty()) {
            Candidate candidate = candidateRepository.findByEmail(email)
                    .orElseThrow(() -> new EntityNotFoundException("Candidate not found"));

            return candidateMapper.toPrivateDto(candidate);
        }

        return null;
    }

    @Override
    public CandidatePublicResponseDto getPublicData(String candidateKeyId) {
        String email = securityContextService.getSecurityContext();

        if (email != null && !email.isEmpty()) {
            Candidate candidate = candidateRepository.findByKeycloakId(candidateKeyId);

            boolean isMe = Objects.equals(candidateKeyId, candidate.getKeycloakId());

            if (isMe) {
                return candidateMapper.toPublicDto(candidate);
            }

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));

            if (user.getAccountStatus() == AccountStatus.REJECTED || user.getUserType() != UserType.RECRUITER) {
                throw new AccessInfoDeniedException("You don't have access to this information");
            }

            return candidateMapper.toPublicDto(candidate);
        }

        return null;
    }

    private CandidateProfileDataResponseDto toDto(CandidateProfileData profileData, List<CandidateLanguage> languages) {
        return new CandidateProfileDataResponseDto(
                profileData.getCandidate().getId(),
                profileData.getDesiredSalary(),
                profileData.getWorkMode(),
                profileData.getAvailableFrom(),
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
