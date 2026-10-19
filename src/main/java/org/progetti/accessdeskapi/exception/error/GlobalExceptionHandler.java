package org.progetti.accessdeskapi.exception.error;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundUserException.class)
    public ResponseEntity<APIError> handle(
            NotFoundUserException exception,
            HttpServletRequest request
    )
    {
        ErrorName err = ErrorName.USER_NOT_FOUND;
        var apiError = new APIError(
                err,
                exception.getMessage(),
                request.getRequestURI(),
                Instant.now(),
                err.getStatus()
        );
        return ResponseEntity
                .status(err.getStatus())
                .body(apiError);
    }

}
