package com.tsfintech.rest.core.operator;

/**
 * Created by jack on 14-7-28.
 */
public interface EntityUpdater<T> {
    public Object update(String scope, String id, T entity);
}
