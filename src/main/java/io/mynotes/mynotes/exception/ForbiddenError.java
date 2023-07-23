package io.mynotes.mynotes.exception;

public class ForbiddenError extends RuntimeException {
    public ForbiddenError(String message) {
        super(message);
    }
}
