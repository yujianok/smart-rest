package com.tsfintech.rest.core.delegate;

import com.tsfintech.rest.core.model.EntityQuery;

/**
 * Created by jack on 16-5-17.
 */
public interface QueryBeanDelegate {

    boolean hasSpecificMethod(Object bean, String methodName, EntityQuery entityQuery);

    <T> T invokeSpecificMethod(Object bean, String methodName, EntityQuery entityQuery);
}
