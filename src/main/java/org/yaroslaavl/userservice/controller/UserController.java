package org.yaroslaavl.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.yaroslaavl.userservice.dto.read.CandidateProfileDataReadDto;
import org.yaroslaavl.userservice.dto.read.CandidateReadDto;
import org.yaroslaavl.userservice.dto.read.RecruiterReadDto;
import org.yaroslaavl.userservice.dto.request.*;
import org.yaroslaavl.userservice.service.CandidateService;
import org.yaroslaavl.userservice.service.RecruiterService;
import org.yaroslaavl.userservice.service.UserService;
import org.yaroslaavl.userservice.validation.groups.CandidateAction;
import org.yaroslaavl.userservice.validation.groups.EditAction;
import org.yaroslaavl.userservice.validation.groups.RecruiterAction;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/")
public class UserController {

    private final UserService userService;
    private final CandidateService candidateService;
    private final RecruiterService recruiterService;

    @GetMapping("/exists")
    public Boolean existsAccount(@RequestParam("email") String email) {
        return userService.existsAccount(email);
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
    public ResponseEntity<CandidateProfileDataReadDto> updateProfileData(@RequestBody CandidateProfileDataRequest candidateProfileDataRequest) {
        CandidateProfileDataReadDto candidateProfileDataReadDto = candidateService.updateCandidateProfileData(candidateProfileDataRequest);
        return ResponseEntity.ok(candidateProfileDataReadDto);
    }

    @PatchMapping("/candidate-info")
    public ResponseEntity<CandidateReadDto> updateCandidateInfo(@RequestBody @Validated({EditAction.class, CandidateAction.class}) CandidateInfoRequest candidateInfoRequest) {
        CandidateReadDto candidateReadDto = candidateService.updateUserInfo(candidateInfoRequest);
        return ResponseEntity.ok(candidateReadDto);
    }

    @PatchMapping("/recruiter-info")
    public ResponseEntity<RecruiterReadDto> updateRecruiterInfo(@RequestBody @Validated({EditAction.class, RecruiterAction.class}) RecruiterPositionRequest recruiterPositionRequest) {
        RecruiterReadDto recruiterReadDto = recruiterService.updateUserInfo(recruiterPositionRequest);
        return ResponseEntity.ok(recruiterReadDto);
    }
}
