package io.mynotes.mynotes.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BadRequestError.class)
    public ResponseEntity<ApiError> badRequestError(BadRequestError e) {
        logger.info("Error :: " + e.getMessage());

        ApiError error = new ApiError(
                "BAD_REQUEST",
                e.getMessage()
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedError.class)
    public ResponseEntity<ApiError> unauthorizedError(UnauthorizedError e) {
        logger.info("Error :: " + e.getMessage());

        ApiError error = new ApiError(
                "UNAUTHORIZED",
                "Invalid credentials.");

        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ForbiddenError.class)
    public ResponseEntity<ApiError> forbiddenError(ForbiddenError e) {
        logger.info("Error :: " + e.getMessage());

        ApiError error = new ApiError(
                "FORBIDDEN",
                e.getMessage()
        );

        return  new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NotFoundError.class)
    public ResponseEntity<ApiError> notFoundError(NotFoundError e) {
        logger.info("Error :: " + e.getMessage());

        ApiError error = new ApiError(
                "NOT_FOUND",
                e.getMessage()
        );

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConflictError.class)
    public ResponseEntity<ApiError> conflictError(ConflictError e) {
        logger.info("Error :: " + e.getMessage());

        ApiError error = new ApiError(
                "CONFLICT",
                e.getMessage()
        );

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleExceptions(Exception e) {
        logger.info("Error :: " + e.getMessage());

        ApiError error = new ApiError(
                "INTERNAL_ERROR",
                "Something went wrong, please contact support team and quote the error_id for assistance");

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
