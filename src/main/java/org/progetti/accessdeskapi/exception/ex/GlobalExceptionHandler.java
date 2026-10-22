package org.progetti.accessdeskapi.exception.ex;

import jakarta.servlet.http.HttpServletRequest;
import org.progetti.accessdeskapi.exception.error.APIError;
import org.progetti.accessdeskapi.exception.error.ErrorName;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Instant now = Instant.now();

    @ExceptionHandler(NotFoundUserException.class)
    public ResponseEntity<APIError> handleNotFoundUser(
            NotFoundUserException exception,
            HttpServletRequest request
    )
    {
        ErrorName err = ErrorName.USER_NOT_FOUND;
        var apiError = new APIError(
                err,
                exception.getMessage(),
                request.getRequestURI(),
                now,
                err.getStatus()
        );
        return ResponseEntity
                .status(err.getStatus())
                .body(apiError);
    }

    @ExceptionHandler(RequestAlreadyDecidedException.class)
    public ResponseEntity<APIError> handleRequestAlreadyDecided(
            RequestAlreadyDecidedException exception,
            HttpServletRequest request
    )
    {
        var err = ErrorName.REQUEST_ALREADY_DECIDED;
        var status = err.getStatus();
        var apiError = new APIError(
                err,
                exception.getMessage(),
                request.getRequestURI(),
                now,
                status
        );

        return ResponseEntity.status(status).body(apiError);
    }

}
