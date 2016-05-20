package com.tsfintech.rest.spring.manager;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.tsfintech.rest.core.delegate.QueryBeanDelegate;
import com.tsfintech.rest.core.exception.RestServiceException;
import com.tsfintech.rest.core.manager.EntityManager;
import com.tsfintech.rest.core.meta.RestMetaUtil;
import com.tsfintech.rest.core.model.EntityQuery;
import com.tsfintech.rest.core.util.StringValueUtil;
import com.tsfintech.rest.spring.dao.EntityQuerySupportDao;

/**
 * Created by jack on 14-12-12.
 */
public class SpringJpaSupportedManager implements EntityManager<Object>, ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(SpringJpaSupportedManager.class);

    private EntityQuerySupportDao entityQuerySupportDao;

    private QueryBeanDelegate queryBeanDelegate;

    private ApplicationContext applicationContext;

    @Override
    public Object save(String scope, Class<Object> type, Object entity) {
        String entityScopeKey = getScopeKey(type);

        try {
            PropertyDescriptor propertyDescriptor = BeanUtils.getPropertyDescriptor(type, entityScopeKey);
            if (propertyDescriptor == null) {
                throw new RestServiceException("Default manager not support save entity without scope key: " + type.getName());
            }

            Class<?> propertyType = propertyDescriptor.getPropertyType();
            Object scopeValue = StringValueUtil.getObjectValue(scope, propertyType);
            Object entityScope = propertyDescriptor.getReadMethod().invoke(entity);
            if (!entityScope.equals(scopeValue)) {
                throw new RestServiceException("Scope not consistent, entity scope " + entityScope + " request scope " + scope);
            }

        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.warn("Can not assign scope value {} to entity {} while saving", scope, type);
        }

        entityQuerySupportDao.save(entity);

        return entity;
    }


    @Override
    public Object retrieve(String scope, Class<Object> type, String id) {
        String entityPrimaryKey = getPrimaryKey(type);
        String entityScopeKey = getScopeKey(type);

        EntityQuery query = EntityQuery.buildQuery();
        if (entityPrimaryKey != null) {
            query.addCondition(entityPrimaryKey, id);
        }
        if (entityScopeKey != null) {
            query.addCondition(entityScopeKey, scope);
        }

        List<Object> results = entityQuerySupportDao.find(type, query);

        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public Object update(String scope, Class<Object> type, String id, Object entity) {
        String entityScopeKey = getScopeKey(type);

        try {
            PropertyDescriptor propertyDescriptor = BeanUtils.getPropertyDescriptor(type, entityScopeKey);
            if (propertyDescriptor == null) {
                throw new RestServiceException("Default manager not support update entity without scope key: " + type.getName());
            }
            Class<?> propertyType = propertyDescriptor.getPropertyType();
            Object scopeValue = StringValueUtil.getObjectValue(scope, propertyType);
            Object entityScope = propertyDescriptor.getReadMethod().invoke(entity);
            if (!scopeValue.equals(entityScope)) {
                throw new RestServiceException("Scope not consistent, entity scope " + entityScope + " request scope " + scope);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.warn("Can not assign scope value {} to entity {} while saving", scope, type);
        }

        return entityQuerySupportDao.update(entity);
    }

    @Override
    public Object delete(String scope, Class<Object> type, String id) {
        String entityScopeKey = getScopeKey(type);
        PropertyDescriptor propertyDescriptor = BeanUtils.getPropertyDescriptor(type, entityScopeKey);
        if (propertyDescriptor == null) {
            throw new RestServiceException("Default manager not support delete entity without scope key: " + type.getName());
        }

        Object entityInDb = retrieve(scope, type, id);
        if (entityInDb != null) {
            entityQuerySupportDao.delete(entityInDb);
            return entityInDb;
        }

        return null;
    }

    @Override
    public long count(String scope, Class<Object> type, EntityQuery entityQuery) {
        String queryBy = entityQuery.getQueryBy();
        String entityScopeKey = getScopeKey(type);
        if (queryBy != null) {
            String scopeCondition = entityQuery.getConditionValue(entityScopeKey);
            if (entityScopeKey == null || (scopeCondition != null && scopeCondition.equals(scope))) {
                String daoName = StringUtils.uncapitalize(type.getSimpleName()) + "Dao";

                Object daoBean = applicationContext.getBean(daoName);
                return queryBeanDelegate.invokeSpecificMethod(daoBean, queryBy, entityQuery);
            } else {
                throw new RestServiceException("Default manager not support count entity without scope key: " + type.getName());
            }
        } else {
            if (BeanUtils.getPropertyDescriptor(type, entityScopeKey) != null) {
                entityQuery.addCondition(entityScopeKey, scope);
            }
            return entityQuerySupportDao.count(type, entityQuery);
        }
    }


    @Override
    public Object find(String scope, Class<Object> type, EntityQuery entityQuery) {
        String queryBy = entityQuery.getQueryBy();
        String entityScopeKey = getScopeKey(type);
        if (queryBy != null) {
            String scopeCondition = entityQuery.getConditionValue(entityScopeKey);
            if (entityScopeKey == null || (scopeCondition != null && scopeCondition.equals(scope))) {
                String daoName = StringUtils.uncapitalize(type.getSimpleName()) + "Dao";

                Object daoBean = applicationContext.getBean(daoName);

                return queryBeanDelegate.invokeSpecificMethod(daoBean, queryBy, entityQuery);
            } else {
                throw new RestServiceException("Default manager not support find entity without scope key: " + type.getName());
            }
        } else {
            if (BeanUtils.getPropertyDescriptor(type, entityScopeKey) != null) {
                entityQuery.addCondition(entityScopeKey, scope);
            }
            return entityQuerySupportDao.find(type, entityQuery);
        }
    }

    private String getScopeKey(Class<Object> type) {
        String entityScopeKey = RestMetaUtil.getScopeKey(type);

        return entityScopeKey;
    }

    private String getPrimaryKey(Class<Object> type) {
        String primaryKey = RestMetaUtil.getPrimaryKey(type);

        return primaryKey;
    }

    public void setEntityQuerySupportDao(EntityQuerySupportDao entityQuerySupportDao) {
        this.entityQuerySupportDao = entityQuerySupportDao;
    }

    public void setQueryBeanDelegate(QueryBeanDelegate queryBeanDelegate) {
        this.queryBeanDelegate = queryBeanDelegate;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}

