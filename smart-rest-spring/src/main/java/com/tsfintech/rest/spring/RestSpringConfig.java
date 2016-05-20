package com.tsfintech.rest.spring;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.tsfintech.rest.core.delegate.QueryBeanDelegate;
import com.tsfintech.rest.core.dispatcher.RestRequestDispatcher;
import com.tsfintech.rest.core.event.RestEventListenerContainer;
import com.tsfintech.rest.core.manager.EntityManager;
import com.tsfintech.rest.core.marshaller.EntityJsonMarshaller;
import com.tsfintech.rest.core.model.EntityClassFinder;
import com.tsfintech.rest.spring.dao.EntityQuerySupportDao;
import com.tsfintech.rest.spring.delegate.SpringQueryBeanDelegate;
import com.tsfintech.rest.spring.factory.NameMatchEntityManagerFactory;
import com.tsfintech.rest.spring.factory.NameMatchEntityOperationFactory;
import com.tsfintech.rest.spring.finder.EntityClassFinderImpl;
import com.tsfintech.rest.spring.manager.SpringJpaSupportedManager;

/**
 * Created by jack on 14-9-3.
 */
@Configuration
@ComponentScan("com.tsfintech.rest.spring.controller")
public class RestSpringConfig extends WebMvcConfigurerAdapter {

    @Value("${rest.service.entity.packages}")
    private String[] entityPackages;


    @Bean(autowire = Autowire.BY_NAME)
    public RestRequestDispatcher restRequestDispatcher() {
        RestRequestDispatcher restRequestDispatcher = new RestRequestDispatcher();
        restRequestDispatcher.setDefaultEntityManager(springJpaSupportedManager());
        restRequestDispatcher.setSpecificEntityManagerFactory(nameMatchEntityManagerFactory());
        restRequestDispatcher.setSpecificEntityOperationFactory(nameMatchEntityOperationFactory());
        restRequestDispatcher.setEntityClassFinder(entityClassFinder());
        restRequestDispatcher.setRestEventListenerContainer(eventListenerContainer());
        restRequestDispatcher.setQueryBeanDelegate(queryBeanDelegate());

        return restRequestDispatcher;
    }

    @Bean
    public EntityJsonMarshaller entityJsonMarshaller() {
        return new EntityJsonMarshaller();
    }

    @Bean
    public EntityClassFinder entityClassFinder() {
        return new EntityClassFinderImpl(entityPackages);
    }

    @Bean(autowire = Autowire.BY_TYPE)
    public EntityManager springJpaSupportedManager() {
        SpringJpaSupportedManager springJpaSupportedManager = new SpringJpaSupportedManager();
        springJpaSupportedManager.setEntityQuerySupportDao(entityQuerySupportDao());
        springJpaSupportedManager.setQueryBeanDelegate(queryBeanDelegate());

        return springJpaSupportedManager;
    }

    @Bean
    public NameMatchEntityManagerFactory nameMatchEntityManagerFactory() {
        return new NameMatchEntityManagerFactory();
    }

    @Bean
    public NameMatchEntityOperationFactory nameMatchEntityOperationFactory() {
        return new NameMatchEntityOperationFactory();
    }

    @Bean
    public EntityQuerySupportDao entityQuerySupportDao() {
        return new EntityQuerySupportDao();
    }

    @Bean
    public QueryBeanDelegate queryBeanDelegate() {
        QueryBeanDelegate queryBeanDelegate = new SpringQueryBeanDelegate();

        return queryBeanDelegate;
    }

    @Bean(autowire = Autowire.BY_TYPE)
    public RestEventListenerContainer eventListenerContainer() {
        return new RestEventListenerContainer();
    }


}
