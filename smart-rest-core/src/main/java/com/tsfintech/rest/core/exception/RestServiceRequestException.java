package com.tsfintech.rest.core.exception;

/**
 * Created by jack on 14-9-25.
 */
public class RestServiceRequestException extends RuntimeException {

    public RestServiceRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
