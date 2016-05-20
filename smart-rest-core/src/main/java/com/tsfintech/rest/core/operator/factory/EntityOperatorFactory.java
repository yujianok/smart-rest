package com.tsfintech.rest.core.operator.factory;

import com.tsfintech.rest.core.model.EntityUri;
import com.tsfintech.rest.core.operator.*;

/**
 * Created by jack on 14-7-29.
 */
public interface EntityOperatorFactory {

    public EntityCreator<Object> getCreator(EntityUri entityUri);

    public EntityUpdater<Object> getUpdater(EntityUri entityUri);

    public EntityFinder<Object> getFinder(EntityUri entityUri);

    public EntityDeleter<Object> getDeleter(EntityUri entityUri);

    public EntityRetriever<Object> getRetriever(EntityUri entityUri);

    public EntityInvoker<Object> getInvoker(EntityUri entityUri);
}
