package com.tsfintech.rest.core.operator;

/**
 * Created by jack on 14-7-28.
 */
public interface EntityCreator<T> {

    public Object save(String scope, T entity);

}
