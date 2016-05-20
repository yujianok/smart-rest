package com.tsfintech.rest.client.request;

import com.tsfintech.rest.client.RestServiceClient;

/**
 * Created by jack on 15-5-5.
 */
public class OperationServiceRequest<T> extends EntityServiceRequest<T> {

    protected String operation;

    public OperationServiceRequest(RestServiceClient restServiceClient,
                            String scope,
                            Class<T> entityType,
                            String id,
                            String operation) {
        super(restServiceClient, scope, entityType, id);

        this.operation = operation;
    }

    public T operate(Object[] args) {
        return restServiceClient.operate(scope, entityType, id, operation, args, entityType);
    }

    public <C> C operate(Object[] args, Class<C> returnType) {
        return restServiceClient.operate(scope, entityType, id, operation, args, returnType);
    }
}
