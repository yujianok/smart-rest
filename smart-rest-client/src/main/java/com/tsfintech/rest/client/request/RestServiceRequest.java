package com.tsfintech.rest.client.request;

import java.util.List;

import com.tsfintech.rest.client.RestServiceClient;
import com.tsfintech.rest.core.model.EntityQuery;

/**
 * Created by jack on 15-5-5.
 */
public class RestServiceRequest<T> {

    protected RestServiceClient restServiceClient;

    protected String scope;

    protected Class<T> entityType;

    protected String id;

    protected String operation;

    public RestServiceRequest(RestServiceClient restServiceClient, String scope, Class<T> entityType) {
        this.restServiceClient = restServiceClient;
        this.scope = scope;
        this.entityType = entityType;
    }

    public EntityServiceRequest<T> entity(String id) {
        return new EntityServiceRequest<T>(restServiceClient, scope, entityType, id);
    }

    public List<T> find(EntityQuery query) {
        return restServiceClient.find(scope, entityType, query);
    }

    public long count(EntityQuery query) {
        return restServiceClient.count(scope, entityType, query);
    }

    public <C> C save(T entity, Class<C> returnType) {
        return restServiceClient.save(scope, entity, returnType);
    }

    public T save(T entity) {
        return restServiceClient.save(scope, entity, entityType);
    }

}
