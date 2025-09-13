package org.yaroslaavl.userservice.exception.handler;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.yaroslaavl.userservice.exception.*;
import org.yaroslaavl.userservice.dto.ErrorResponse;
import org.yaroslaavl.userservice.exception.error.ErrorType;

import java.util.Locale;
import java.util.Optional;
import java.util.Set;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException ef, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                ef.getMessage(),
                ErrorType.ENTITY_NOT_FOUND,
                request.getRequestURI());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(UserAccountStatusException.class)
    public ResponseEntity<ErrorResponse> handleUserAccountStatusException(UserAccountStatusException uas, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
          uas.getMessage(),
          ErrorType.TEMPORARY_BLOCKED,
          request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(AuthLoginException.class)
    public ResponseEntity<ErrorResponse> handleAuthLogin(AuthLoginException al, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                al.getMessage(),
                ErrorType.AUTH_LOGIN,
                request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<ErrorResponse> handleFileStorage(FileStorageException fs, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                fs.getMessage(),
                ErrorType.FILE_STORAGE,
                request.getRequestURI());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(ImageUploadException.class)
    public ResponseEntity<ErrorResponse> handleImageUpload(ImageUploadException iu, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                iu.getMessage(),
                ErrorType.IMAGE_UPLOAD,
                request.getRequestURI());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(LanguagePreviousDataDeletionException.class)
    public ResponseEntity<ErrorResponse> handleLanguagePreviousDataDeletion(LanguagePreviousDataDeletionException lpd, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                lpd.getMessage(),
                ErrorType.LANGUAGE_DELETION,
                request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
