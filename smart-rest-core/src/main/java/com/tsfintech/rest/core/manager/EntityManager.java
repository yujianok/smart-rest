package com.tsfintech.rest.core.manager;

import com.tsfintech.rest.core.model.EntityQuery;

/**
 * Created by jack on 14-7-29.
 */
public interface EntityManager<T> {

    public Object save(String scope, Class<T> type, T entity);

    public T retrieve(String scope, Class<T> type, String id);

    public Object update(String scope, Class<T> type, String id, T entity);

    public T delete(String scope, Class<T> type, String id);

    public long count(String scope, Class<T> type, EntityQuery entityQuery);

    public T find(String scope, Class<T> type, EntityQuery entityQuery);
}
