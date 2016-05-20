package com.tsfintech.rest.core.manager.factory;

import com.tsfintech.rest.core.manager.EntityManager;
import com.tsfintech.rest.core.model.EntityUri;

/**
 * Created by jack on 14-7-29.
 */
public interface EntityManagerFactory {

    EntityManager<Object> getEntityManager(EntityUri entityUri);

}
