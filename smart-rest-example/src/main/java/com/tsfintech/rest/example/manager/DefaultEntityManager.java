package com.tsfintech.rest.example.manager;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;

import com.tsfintech.rest.core.manager.EntityManager;
import com.tsfintech.rest.core.model.EntityQuery;
import com.tsfintech.rest.example.dao.CommonDao;
import com.tsfintech.rest.example.util.QueryConverter;

/**
 * Created by jack on 14-10-15.
 */
public class DefaultEntityManager implements EntityManager<Object> {

    @Autowired
    private CommonDao commonDao;

    @Override
    public Object save(String scope, Class<Object> type, Object entity) {
        System.out.println(DefaultEntityManager.class.getName() + "#save");
        commonDao.save(entity);
        return entity;
    }

    @Override
    public Object retrieve(String scope, Class<Object> type, String id) {
        System.out.println(DefaultEntityManager.class.getName() + "#retrieve");
        return commonDao.retrieve(type, Long.parseLong(id));
    }

    @Override
    public Object update(String scope, Class<Object> type, String id, Object entity) {
        System.out.println(DefaultEntityManager.class.getName() + "#update");
        commonDao.update(entity);

        return entity;
    }

    @Override
    public Object delete(String scope, Class<Object> type, String id) {
        System.out.println(DefaultEntityManager.class.getName() + "#delete");
        Object entity = commonDao.retrieve(type, Long.parseLong(id));
        commonDao.delete(entity);

        return entity;
    }

    @Override
    public long count(String scope, Class<Object> type, EntityQuery entityQuery) {
        System.out.println(DefaultEntityManager.class.getName() + "#count");
        DetachedCriteria criteria = QueryConverter.convertToCriteria(type, entityQuery);

        return commonDao.count(criteria);
    }

    @Override
    public List<Object> find(String scope, Class<Object> type, EntityQuery entityQuery) {
        System.out.println(DefaultEntityManager.class.getName() + "#find");
        DetachedCriteria criteria = QueryConverter.convertToCriteria(type, entityQuery);

        return commonDao.find(criteria, entityQuery.getPageNo(), entityQuery.getPageSize());
    }
}
