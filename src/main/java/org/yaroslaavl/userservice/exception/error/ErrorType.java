package org.yaroslaavl.userservice.exception.error;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "ErrorType")
public enum ErrorType {

    APPROVAL_REQUIRED,
    ALREADY_REGISTERED,
    AUTH_LOGIN,

    TEMPORARY_BLOCKED,

    FILE_STORAGE,
    IMAGE_UPLOAD,

    KEY_CLOAK_ACTION,

    LANGUAGE_DELETION,
    RECRUITER_NO_UPDATE_PERMISSION,
    RECRUITER_REQUEST_CREATED,

    USER_VERIFICATION_NOT_ACCEPTED,

    ENTITY_NOT_FOUND
}
