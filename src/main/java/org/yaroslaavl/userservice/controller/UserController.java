package org.yaroslaavl.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yaroslaavl.userservice.dto.user.DeleteAccountRequest;
import org.yaroslaavl.userservice.service.impl.UserServiceImpl;
import org.yaroslaavl.userservice.validation.groups.EditAction;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/")
public class UserController {

    private final UserServiceImpl userService;

    @DeleteMapping("/delete")
    public void deleteUserAccount(@RequestBody @Validated(EditAction.class) DeleteAccountRequest deleteAccountRequest) {
        userService.deleteAccount(deleteAccountRequest);
    }

}
