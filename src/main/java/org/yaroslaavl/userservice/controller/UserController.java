package org.yaroslaavl.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.yaroslaavl.userservice.database.entity.enums.profile.AvailableFrom;
import org.yaroslaavl.userservice.database.entity.enums.profile.Salary;
import org.yaroslaavl.userservice.database.entity.enums.profile.WorkMode;
import org.yaroslaavl.userservice.dto.response.*;
import org.yaroslaavl.userservice.dto.request.*;
import org.yaroslaavl.userservice.feignClient.dto.UserFeignDto;
import org.yaroslaavl.userservice.service.CandidateService;
import org.yaroslaavl.userservice.service.RecruiterService;
import org.yaroslaavl.userservice.service.UserService;
import org.yaroslaavl.userservice.validation.groups.CandidateAction;
import org.yaroslaavl.userservice.validation.groups.EditAction;
import org.yaroslaavl.userservice.validation.groups.RecruiterAction;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;
    private final CandidateService candidateService;
    private final RecruiterService recruiterService;

    @GetMapping("/exists")
    public Boolean existsAccount(@RequestParam("email") String email) {
        return userService.existsAccount(email);
    }

    @GetMapping("/isApproved")
    public Boolean isApproved(@RequestParam("userId") String userId) {
        return userService.isAccountApproved(userId);
    }

    @GetMapping("/batch-displayName")
    public ResponseEntity<Map<String, String>> usersDisplayName(@RequestParam("userIds") Set<String> userIds,
                                                          @RequestParam("currentUserEmail") String currentUserEmail) {
        return ResponseEntity.ok(userService.usersDisplayName(userIds, currentUserEmail));
    }

    @GetMapping("/filtered-candidates")
    public ResponseEntity<Map<String, UserFeignDto>> getFilteredCandidates(
            @RequestParam(required = false) Salary salary,
            @RequestParam(required = false) WorkMode workMode,
            @RequestParam(required = false) Integer availableHoursPerWeek,
            @RequestParam(required = false) AvailableFrom availableFrom
    ) {
        return ResponseEntity.ok(candidateService.getFilteredCandidates(salary, workMode, availableHoursPerWeek, availableFrom));
    }

    @GetMapping("/belongs")
    public Boolean belongsToCompany(@RequestParam("recruiterKeyId") String recruiterKeyId,
                                    @RequestParam("companyId") UUID companyId) {
        return recruiterService.isRecruiterBelongToCompany(recruiterKeyId, companyId);
    }

    @DeleteMapping("/account")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteUserAccount(@RequestBody @Validated(EditAction.class) DeleteAccountRequest deleteAccountRequest) {
        userService.deleteAccount(deleteAccountRequest);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changeUserPassword(
            @RequestBody @Validated(EditAction.class) ChangePasswordRequest updatePasswordDto) {
        boolean isUpdated = userService.updatePassword(updatePasswordDto);
        return ResponseEntity.ok(isUpdated ? "Password updated successfully" : "Password update error");
    }

    @PatchMapping("/profile-data")
    public ResponseEntity<CandidateProfileDataResponseDto> updateProfileData(@RequestBody CandidateProfileDataRequest candidateProfileDataRequest) {
        CandidateProfileDataResponseDto candidateProfileDataResponseDto = candidateService.updateCandidateProfileData(candidateProfileDataRequest);
        return ResponseEntity.ok(candidateProfileDataResponseDto);
    }

    @PatchMapping("/candidate-info")
    public ResponseEntity<CandidateResponseDto> updateCandidateInfo(@RequestBody @Validated({EditAction.class, CandidateAction.class}) CandidateInfoRequest candidateInfoRequest) {
        CandidateResponseDto candidateResponseDto = candidateService.updateUserInfo(candidateInfoRequest);
        return ResponseEntity.ok(candidateResponseDto);
    }

    @PatchMapping("/recruiter-info")
    public ResponseEntity<RecruiterResponseDto> updateRecruiterInfo(@RequestBody @Validated({EditAction.class, RecruiterAction.class}) RecruiterPositionRequest recruiterPositionRequest) {
        RecruiterResponseDto recruiterResponseDto = recruiterService.updateUserInfo(recruiterPositionRequest);
        return ResponseEntity.ok(recruiterResponseDto);
    }

    @GetMapping("/me-candidate")
    public ResponseEntity<CandidatePrivateResponseDto> getMe() {
        return ResponseEntity.ok(candidateService.getPersonalData());
    }

    @GetMapping("me-recruiter")
    public ResponseEntity<RecruiterPrivateResponseDto> getMeRecruiter() {
        return ResponseEntity.ok(recruiterService.getPersonalData());
    }

    @GetMapping("/public-candidate")
    public ResponseEntity<CandidatePublicResponseDto> getCandidatePublicData(@RequestParam("candidateKeyId") String candidateKeyId) {
        return ResponseEntity.ok(candidateService.getPublicData(candidateKeyId));
    }

    @GetMapping("/public-recruiter")
    public ResponseEntity<RecruiterPublicResponseDto> getRecruiterPublicData(@RequestParam("recruiterKeyId") String recruiterKeyId) {
        return ResponseEntity.ok(recruiterService.getPublicData(recruiterKeyId));
    }
}
