package org.progetti.accessdeskapi.exception.ex;

public class NotFoundUserException extends RuntimeException {
    public NotFoundUserException(String message) {
        super(message);
    }
}
