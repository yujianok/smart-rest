package com.tsfintech.rest.spring.factory;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.tsfintech.rest.core.model.EntityUri;
import com.tsfintech.rest.core.operator.*;
import com.tsfintech.rest.core.operator.factory.EntityOperatorFactory;

/**
 * Created by jack on 14-7-30.
 */
public class NameMatchEntityOperationFactory implements EntityOperatorFactory, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public EntityCreator<Object> getCreator(EntityUri entityUri) {
        String entityName = entityUri.getEntityName();
        return locateServiceBean(entityName, EntityCreator.class);
    }

    @Override
    public EntityUpdater<Object> getUpdater(EntityUri entityUri) {
        String entityName = entityUri.getEntityName();
        return locateServiceBean(entityName, EntityUpdater.class);
    }

    @Override
    public EntityFinder<Object> getFinder(EntityUri entityUri) {
        String entityName = entityUri.getEntityName();
        return locateServiceBean(entityName, EntityFinder.class);
    }

    @Override
    public EntityDeleter<Object> getDeleter(EntityUri entityUri) {
        String entityName = entityUri.getEntityName();
        return locateServiceBean(entityName, EntityDeleter.class);
    }

    @Override
    public EntityRetriever<Object> getRetriever(EntityUri entityUri) {
        String entityName = entityUri.getEntityName();
        return locateServiceBean(entityName, EntityRetriever.class);
    }

    @Override
    public EntityInvoker<Object> getInvoker(EntityUri entityUri) {
        String entityName = entityUri.getEntityName();
        return locateServiceBean(entityName, EntityInvoker.class);
    }

    private <T> T locateServiceBean(String entityName, Class<? extends T> type) {
        try {
            String beanName = StringUtils.uncapitalize(entityName) + type.getSimpleName().substring("Entity".length());
            return applicationContext.getBean(beanName, type);
        } catch (NoSuchBeanDefinitionException | BeanNotOfRequiredTypeException e) {
            try {
                String beanName = StringUtils.uncapitalize(entityName + "Manager");
                return applicationContext.getBean(beanName, type);
            } catch (NoSuchBeanDefinitionException | BeanNotOfRequiredTypeException e1) {
                return null;
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
