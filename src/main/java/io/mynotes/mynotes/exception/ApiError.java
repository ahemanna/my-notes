package io.mynotes.mynotes.exception;

import io.mynotes.api.management.model.Error;

import java.time.OffsetDateTime;
import java.util.UUID;

public class ApiError extends Error {
    public ApiError() {
        super();
        this.setErrorId(UUID.randomUUID());
        this.setTimestamp(OffsetDateTime.now());
    }

    public ApiError(String errorCode) {
        this();
        this.setErrorCode(errorCode);
    }

    public ApiError(String errorCode, String errorDescription) {
        this(errorCode);
        this.setErrorDescription(errorDescription);
    }
}
