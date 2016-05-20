package com.tsfintech.rest.core.dispatcher;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.tsfintech.rest.core.delegate.QueryBeanDelegate;
import com.tsfintech.rest.core.event.RestEventListenerContainer;
import com.tsfintech.rest.core.event.RestEventType;
import com.tsfintech.rest.core.exception.RestServiceException;
import com.tsfintech.rest.core.exception.RestServiceUnavailableException;
import com.tsfintech.rest.core.manager.EntityManager;
import com.tsfintech.rest.core.manager.factory.EntityManagerFactory;
import com.tsfintech.rest.core.meta.RestMetaUtil;
import com.tsfintech.rest.core.model.EntityClassFinder;
import com.tsfintech.rest.core.model.EntityQuery;
import com.tsfintech.rest.core.model.EntityUri;
import com.tsfintech.rest.core.operator.factory.EntityOperatorFactory;
import com.tsfintech.rest.core.util.OperationHelper;
import com.tsfintech.rest.core.operator.*;

/**
 * Created by jack on 14-7-29.
 */
@SuppressWarnings("unchecked")
public class RestRequestDispatcher {

    private EntityManager<Object> defaultEntityManager;

    private EntityManagerFactory specificEntityManagerFactory;

    private EntityOperatorFactory specificEntityOperationFactory;

    private EntityClassFinder entityClassFinder;

    private RestEventListenerContainer restEventListenerContainer;

    private QueryBeanDelegate queryBeanDelegate;

    public Object doCreate(EntityUri entityUri) {
        Class<?> entityClass = entityClassFinder.findClass(entityUri);
        if (entityClass == null) {
            throw new RestServiceUnavailableException(entityUri, "initial");
        }

        try {
            return entityClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RestServiceUnavailableException(entityUri, "initial");
        }
    }

    public Object doSave(EntityUri entityUri, Object entity) {
        EntityCreator<Object> entityCreator = specificEntityOperationFactory == null ? null : specificEntityOperationFactory.getCreator(entityUri);
        EntityManager<Object> specificEntityManager = specificEntityManagerFactory == null ? null : specificEntityManagerFactory.getEntityManager(entityUri);

        Class entityClass = entityClassFinder.findClass(entityUri);
        if (entityClass == null) {
            throw new RestServiceUnavailableException(entityUri, "save");
        }

        Object result;
        if (entityCreator != null) {
            result = entityCreator.save(entityUri.getEntityScope(), entity);
        } else if (specificEntityManager != null) {
            result = specificEntityManager.save(entityUri.getEntityScope(), entityClass, entity);
        } else if (defaultEntityManager != null && RestMetaUtil.defaultSave(entityClass)) {
            result = defaultEntityManager.save(entityUri.getEntityScope(), entityClass, entity);
        } else {
            throw new RestServiceUnavailableException(entityUri, "save");
        }

        restEventListenerContainer.fireEvent(entityUri, RestEventType.CREATE, entity);
        return result;
    }

    public Object doRetrieve(EntityUri entityUri) {
        EntityRetriever<Object> entityRetriever = specificEntityOperationFactory == null ? null : specificEntityOperationFactory.getRetriever(entityUri);
        EntityManager<Object> specificEntityManager = specificEntityManagerFactory == null ? null : specificEntityManagerFactory.getEntityManager(entityUri);

        Class entityClass = entityClassFinder.findClass(entityUri);
        if (entityClass == null) {
            throw new RestServiceUnavailableException(entityUri, "retrieve");
        }

        if (entityRetriever != null) {
            return entityRetriever.retrieve(entityUri.getEntityScope(), entityUri.getEntityId());
        } else if (specificEntityManager != null) {
            return specificEntityManager.retrieve(entityUri.getEntityScope(), entityClass, entityUri.getEntityId());
        } else if (defaultEntityManager != null && RestMetaUtil.defaultRetrieve(entityClass)) {
            return defaultEntityManager.retrieve(entityUri.getEntityScope(), entityClass, entityUri.getEntityId());
        } else {
            throw new RestServiceUnavailableException(entityUri, "retrieve");
        }
    }

    public Object doUpdate(EntityUri entityUri, Object entity) {
        EntityUpdater<Object> entityUpdater = specificEntityOperationFactory == null ? null : specificEntityOperationFactory.getUpdater(entityUri);
        EntityManager<Object> specificEntityManager = specificEntityManagerFactory == null ? null : specificEntityManagerFactory.getEntityManager(entityUri);

        Class entityClass = entityClassFinder.findClass(entityUri);
        if (entityClass == null) {
            throw new RestServiceUnavailableException(entityUri, "update");
        }

        Object result;
        if (entityUpdater != null) {
            result = entityUpdater.update(entityUri.getEntityScope(), entityUri.getEntityId(), entity);
        } else if (specificEntityManager != null) {
            result = specificEntityManager.update(entityUri.getEntityScope(), entityClass, entityUri.getEntityId(), entity);
        } else if (defaultEntityManager != null && RestMetaUtil.defaultUpdate(entityClass)) {
            result = defaultEntityManager.update(entityUri.getEntityScope(), entityClass, entityUri.getEntityId(), entity);
        } else {
            throw new RestServiceUnavailableException(entityUri, "update");
        }

        restEventListenerContainer.fireEvent(entityUri, RestEventType.UPDATE, entity);
        return result;
    }

    public Object doDelete(EntityUri entityUri) {
        EntityDeleter<Object> entityDeleter = specificEntityOperationFactory == null ? null : specificEntityOperationFactory.getDeleter(entityUri);
        EntityManager<Object> specificEntityManager = specificEntityManagerFactory == null ? null : specificEntityManagerFactory.getEntityManager(entityUri);

        Class entityClass = entityClassFinder.findClass(entityUri);
        if (entityClass == null) {
            throw new RestServiceUnavailableException(entityUri, "delete");
        }

        Object result;
        if (entityDeleter != null) {
            result = entityDeleter.delete(entityUri.getEntityScope(), entityUri.getEntityId());
        } else if (specificEntityManager != null) {
            result = specificEntityManager.delete(entityUri.getEntityScope(), entityClass, entityUri.getEntityId());
        } else if (defaultEntityManager != null && RestMetaUtil.defaultDelete(entityClass)) {
            result = defaultEntityManager.delete(entityUri.getEntityScope(), entityClass, entityUri.getEntityId());
        } else {
            throw new RestServiceUnavailableException(entityUri, "delete");
        }

        restEventListenerContainer.fireEvent(entityUri, RestEventType.DELETE, result);
        return result;
    }

    public long doCount(EntityUri entityUri, EntityQuery entityQuery) {
        EntityFinder<Object> entityFinder = specificEntityOperationFactory == null ? null : specificEntityOperationFactory.getFinder(entityUri);
        EntityManager<Object> specificEntityManager = specificEntityManagerFactory == null ? null : specificEntityManagerFactory.getEntityManager(entityUri);

        Class entityClass = entityClassFinder.findClass(entityUri);
        if (entityClass == null) {
            throw new RestServiceUnavailableException(entityUri, "count");
        }

        if (entityFinder != null && entityQuery.getQueryBy() == null) {
            return entityFinder.count(entityUri.getEntityScope(), entityQuery);
        } else if (specificEntityManager != null && entityQuery.getQueryBy() == null) {
            return specificEntityManager.count(entityUri.getEntityScope(), entityClass, entityQuery);
        } else if (defaultEntityManager != null && RestMetaUtil.defaultCount(entityClass)) {
            return defaultEntityManager.count(entityUri.getEntityScope(), entityClass, entityQuery);
        } else {
            throw new RestServiceUnavailableException(entityUri, "count");
        }
    }

    public Object doFind(EntityUri entityUri, EntityQuery entityQuery) {
        EntityFinder<Object> entityFinder = specificEntityOperationFactory == null ? null : specificEntityOperationFactory.getFinder(entityUri);
        EntityManager<Object> specificEntityManager = specificEntityManagerFactory == null ? null : specificEntityManagerFactory.getEntityManager(entityUri);

        Class entityClass = entityClassFinder.findClass(entityUri);
        if (entityClass == null) {
            throw new RestServiceUnavailableException(entityUri, "find");
        }

        if (entityFinder != null) {
            String queryBy = entityQuery.getQueryBy();
            if (queryBy != null && queryBeanDelegate.hasSpecificMethod(entityFinder, queryBy, entityQuery)) {
                return queryBeanDelegate.invokeSpecificMethod(entityFinder, queryBy, entityQuery);
            } else {
                return entityFinder.find(entityUri.getEntityScope(), entityQuery);
            }
        }

        if (specificEntityManager != null) {
            String queryBy = entityQuery.getQueryBy();
            if (queryBy != null && queryBeanDelegate.hasSpecificMethod(specificEntityManager, queryBy, entityQuery)) {
                return queryBeanDelegate.invokeSpecificMethod(specificEntityManager, queryBy, entityQuery);
            } else {
                return specificEntityManager.find(entityUri.getEntityScope(), entityClass, entityQuery);
            }
        }

        if (defaultEntityManager != null && RestMetaUtil.defaultFind(entityClass)) {
            return defaultEntityManager.find(entityUri.getEntityScope(), entityClass, entityQuery);
        }

        throw new RestServiceUnavailableException(entityUri, "find");
    }

    public Object doOperation(EntityUri entityUri, String[] args) {
        Class entityClass = entityClassFinder.findClass(entityUri);
        if (entityClass == null) {
            throw new RestServiceUnavailableException(entityUri, "operation");
        }

        EntityInvoker<Object> entityInvoker = specificEntityOperationFactory.getInvoker(entityUri);
        if (entityInvoker == null) {
            throw new RestServiceUnavailableException(entityUri, "operation");
        }

        Object entity = doRetrieve(entityUri);

        String invokerMethod = entityUri.getOperation();
        Method operationMethod = OperationHelper.getInvokerMethod(invokerMethod, entityInvoker, args);
        if (operationMethod == null) {
            throw new RestServiceUnavailableException(entityUri, "operate");
        }

        try {
            Object[] arguments = OperationHelper.buildArguments(operationMethod, entity, args);
            Object result = operationMethod.invoke(entityInvoker, arguments);
            restEventListenerContainer.fireEvent(entityUri, RestEventType.INVOKE, args);

            return result;
        } catch (IllegalAccessException e) {
            throw new RestServiceUnavailableException(entityUri, "operate");
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof RuntimeException) {
                throw (RuntimeException) e.getTargetException();
            } else {
                throw new RestServiceException(e.getMessage(), e);
            }
        }
    }

    public void setDefaultEntityManager(EntityManager<Object> defaultEntityManager) {
        this.defaultEntityManager = defaultEntityManager;
    }

    public void setSpecificEntityManagerFactory(EntityManagerFactory specificEntityManagerFactory) {
        this.specificEntityManagerFactory = specificEntityManagerFactory;
    }

    public void setSpecificEntityOperationFactory(EntityOperatorFactory specificEntityOperationFactory) {
        this.specificEntityOperationFactory = specificEntityOperationFactory;
    }

    public void setEntityClassFinder(EntityClassFinder entityClassFinder) {
        this.entityClassFinder = entityClassFinder;
    }

    public void setRestEventListenerContainer(RestEventListenerContainer restEventListenerContainer) {
        this.restEventListenerContainer = restEventListenerContainer;
    }

    public void setQueryBeanDelegate(QueryBeanDelegate queryBeanDelegate) {
        this.queryBeanDelegate = queryBeanDelegate;
    }
}