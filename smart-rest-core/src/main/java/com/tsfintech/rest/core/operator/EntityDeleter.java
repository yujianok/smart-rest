package com.tsfintech.rest.core.operator;

/**
 * Created by jack on 14-7-28.
 */
public interface EntityDeleter<T> {
    public T delete(String scope, String id);
}
