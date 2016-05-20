package com.tsfintech.rest.client.request;

import com.tsfintech.rest.client.RestServiceClient;

/**
 * Created by jack on 15-5-5.
 */
public class EntityServiceRequest<T> extends RestServiceRequest<T> {

    protected String id;

    public EntityServiceRequest(RestServiceClient restServiceClient,
                         String scope,
                         Class<T> entityType,
                         String id) {
        super(restServiceClient, scope, entityType);
        this.id = id;
    }

    public OperationServiceRequest<T> operation(String operation) {
        return new OperationServiceRequest<T>(restServiceClient, scope, entityType, id, operation);
    }

    public T retrieve() {
        return restServiceClient.retrieve(scope, entityType, id);
    }

    public T update(T entity) {
        return restServiceClient.update(scope, id, entity, entityType);
    }

    public <C> C update(T entity, Class<C> returnType) {
        return restServiceClient.update(scope, id, entity, returnType);
    }

    public T delete() {
        return restServiceClient.delete(scope, entityType, id);
    }

}
