package com.tsfintech.rest.core.exception;

/**
 * Created by jack on 14-7-29.
 */
public class RestServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private int stateCode = 417;

    public RestServiceException(int stateCode, String message, Throwable cause) {
        super(message, cause);
        this.stateCode = stateCode;
    }

    public RestServiceException(int stateCode, String message) {
        super(message);
        this.stateCode = stateCode;
    }

    public RestServiceException(int stateCode) {
        this.stateCode = stateCode;
    }

    public RestServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public RestServiceException(String message) {
        super(message);
    }

    public int getStateCode() {
        return stateCode;
    }
}
