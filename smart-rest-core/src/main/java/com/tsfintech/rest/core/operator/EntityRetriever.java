package com.tsfintech.rest.core.operator;

/**
 * Created by jack on 14-7-28.
 */
public interface EntityRetriever<T> {

    public T retrieve(String scope, String id);

}
