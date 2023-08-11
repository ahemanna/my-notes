package io.mynotes.mynotes.exception;

public class NotFoundError extends RuntimeException{
    public NotFoundError(String message) {
        super(message);
    }
}
