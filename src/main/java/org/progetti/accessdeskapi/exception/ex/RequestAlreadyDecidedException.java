package org.progetti.accessdeskapi.exception.ex;

public class RequestAlreadyDecidedException extends RuntimeException {
    public RequestAlreadyDecidedException(String message) {
        super(message);
    }
}
