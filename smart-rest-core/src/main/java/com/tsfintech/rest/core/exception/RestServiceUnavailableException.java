package com.tsfintech.rest.core.exception;

import com.tsfintech.rest.core.model.EntityUri;

/**
 * Created by jack on 14-7-29.
 */
public class RestServiceUnavailableException extends RestServiceException {

    private static final long serialVersionUID = 1L;

    private EntityUri entityUri;

    private String operation;

    public RestServiceUnavailableException(EntityUri entityUri, String operation) {
        super("Rest service is unavailable: " + operation + " " + entityUri);
        this.entityUri = entityUri;
        this.operation = operation;
    }

    public EntityUri getEntityUri() {
        return entityUri;
    }

    public String getOperation() {
        return operation;
    }
}
