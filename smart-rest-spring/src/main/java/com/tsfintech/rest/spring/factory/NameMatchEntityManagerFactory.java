package com.tsfintech.rest.spring.factory;

import com.tsfintech.rest.core.manager.EntityManager;
import com.tsfintech.rest.core.manager.factory.EntityManagerFactory;
import com.tsfintech.rest.core.model.EntityUri;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created by jack on 14-7-30.
 */
public class NameMatchEntityManagerFactory implements EntityManagerFactory, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public EntityManager<Object> getEntityManager(EntityUri entityUri) {
        String entityName = entityUri.getEntityName();
        String beanName = StringUtils.uncapitalize(entityName + "Manager");
        try {
            return applicationContext.getBean(beanName, EntityManager.class);
        } catch (NoSuchBeanDefinitionException | BeanNotOfRequiredTypeException e) {
            return null;
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
