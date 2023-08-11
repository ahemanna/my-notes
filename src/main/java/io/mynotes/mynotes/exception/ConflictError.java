package io.mynotes.mynotes.exception;

public class ConflictError extends RuntimeException {
    public ConflictError(String message) {
        super(message);
    }
}
