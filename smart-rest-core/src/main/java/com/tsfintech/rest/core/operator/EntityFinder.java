package com.tsfintech.rest.core.operator;

import com.tsfintech.rest.core.model.EntityQuery;

import java.util.List;

/**
 * Created by jack on 14-7-28.
 */
public interface EntityFinder<T> {
    public long count(String scope, EntityQuery entityQuery);

    public List<T> find(String scope, EntityQuery entityQuery);
}
