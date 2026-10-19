package org.progetti.accessdeskapi.exception.error;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorName {

    REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND);

    private final HttpStatus status;

    ErrorName(HttpStatus status) {
        this.status = status;
    }
}
