package io.mynotes.mynotes.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BadRequestError.class)
    public ResponseEntity<ApiError> badRequestError(BadRequestError e) {
        System.out.println("Error :: " + e.getMessage());

        ApiError error = new ApiError(
                "BAD_REQUEST",
                e.getMessage()
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConflictError.class)
    public ResponseEntity<ApiError> conflictError(ConflictError e) {
        System.out.println("Error :: " + e.getMessage());

        ApiError error = new ApiError(
                "CONFLICT",
                e.getMessage()
        );

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UnauthorizedError.class)
    public ResponseEntity<ApiError> unauthorizedError(UnauthorizedError e) {
        System.out.println("Error :: " + e.getMessage());

        ApiError error = new ApiError(
                "UNAUTHORIZED",
                "Invalid credentials.");

        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleExceptions(Exception e) {
        System.out.println("Error :: " + e.getMessage());

        ApiError error = new ApiError(
                "INTERNAL_ERROR",
                "Something went wrong, please contact support team and quote the error_id for assistance");

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
