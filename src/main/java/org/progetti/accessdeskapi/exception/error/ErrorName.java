package org.progetti.accessdeskapi.exception.error;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorName {

    REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND),
    REQUEST_ALREADY_DECIDED(HttpStatus.CONFLICT),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND),
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT);


    private final HttpStatus status;

    ErrorName(HttpStatus status) {
        this.status = status;
    }
}
