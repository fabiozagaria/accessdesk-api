package org.progetti.accessdeskapi.exception.ex;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import org.progetti.accessdeskapi.exception.error.APIError;
import org.progetti.accessdeskapi.exception.error.ErrorName;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundUserException.class)
    public ResponseEntity<APIError> handleNotFoundUser(
            NotFoundUserException exception,
            HttpServletRequest request
    )
    {
        ErrorName err = ErrorName.USER_NOT_FOUND;
        var status = err.getStatus();

        return ResponseEntity
                .status(status)
                .body(istanceObjectError(status, err, exception, request));
    }

    @ExceptionHandler(NotFoundRequestException.class)
    public ResponseEntity<APIError> handleNotFoundRequest(
            NotFoundRequestException exception,
            HttpServletRequest request
    )
    {
        var err = ErrorName.REQUEST_NOT_FOUND;
        var status = err.getStatus();

        return ResponseEntity
                .status(status)
                .body(istanceObjectError(status, err, exception, request));
    }

    @ExceptionHandler(RequestAlreadyDecidedException.class)
    public ResponseEntity<APIError> handleRequestAlreadyDecided(
            RequestAlreadyDecidedException exception,
            HttpServletRequest request
    )
    {
        var err = ErrorName.REQUEST_ALREADY_DECIDED;
        var status = err.getStatus();

        return ResponseEntity.status(status).body(istanceObjectError(status, err, exception, request));
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<APIError> handleUserAlreadyExists(
            UserAlreadyExistsException exception,
            HttpServletRequest request

    ) {
        var err = ErrorName.USER_ALREADY_EXISTS;
        var status = err.getStatus();
        return ResponseEntity
                .status(status)
                .body(istanceObjectError(status, err, exception, request));

    }

    @ExceptionHandler({JwtException.class, UsernameNotFoundException.class})
    public ResponseEntity<APIError> handleJwt(
            RuntimeException exception,
            HttpServletRequest request
    ) {
        ErrorName err = ErrorName.TOKEN_ERROR;
        var status = err.getStatus();
        return ResponseEntity
                .status(status)
                .body(istanceObjectError(
                        status,
                        err,
                        exception,
                        request

                ));
    }

    private APIError istanceObjectError(
            HttpStatus status,
            ErrorName err,
            RuntimeException exception,
            HttpServletRequest request
    ) {
        Instant now = Instant.now();
        return new APIError(
                err,
                exception.getMessage(),
                request.getRequestURI(),
                now,
                status
        );
    }

}
