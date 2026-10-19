package org.progetti.accessdeskapi.exception.error;

import org.springframework.http.HttpStatus;

import java.time.Instant;

public record APIError(
        ErrorName errorName,
        String msg,
        String path,
        Instant timestamp,
        HttpStatus status
) {
}
