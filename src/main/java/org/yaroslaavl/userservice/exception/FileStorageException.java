package org.yaroslaavl.userservice.exception;

import io.minio.errors.MinioException;

public class FileStorageException extends RuntimeException {
    public FileStorageException(String message, MinioException me) {
        super(message, me);
    }
}
