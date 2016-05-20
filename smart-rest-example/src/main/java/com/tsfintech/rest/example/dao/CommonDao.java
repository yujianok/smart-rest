package com.tsfintech.rest.example.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 * Created by jack on 14-9-1.
 */
@Repository
@Transactional
public class CommonDao {

    @PersistenceContext
    private EntityManager entityManager;

    public long count(DetachedCriteria detachedCriteria) {
        Session session = entityManager.unwrap(Session.class);

        Criteria criteria = detachedCriteria.getExecutableCriteria(session);
        criteria.setProjection(Projections.rowCount());

        List<Long> results = criteria.list();
        return results.get(0);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> find(DetachedCriteria detachedCriteria, int pageNo, int pageSize) {
        Session session = entityManager.unwrap(Session.class);

        Criteria criteria = detachedCriteria.getExecutableCriteria(session);
        criteria.setFirstResult((pageNo - 1) * pageSize);
        criteria.setMaxResults(pageSize);

        return criteria.list();
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> find(DetachedCriteria detachedCriteria) {
        Session session = entityManager.unwrap(Session.class);

        Criteria criteria = detachedCriteria.getExecutableCriteria(session);
        return criteria.list();
    }

    public void save(Object entity) {
        Session session = entityManager.unwrap(Session.class);
        session.save(entity);
    }

    public void update(Object entity) {
        Session session = entityManager.unwrap(Session.class);
        session.update(entity);
    }

    public void delete(Object entity) {
        Session session = entityManager.unwrap(Session.class);
        session.delete(entity);
    }

    @SuppressWarnings("unchecked")
    public <T> T retrieve(Class<T> targetClass, long entityId) {
        Session session = entityManager.unwrap(Session.class);
        return (T) session.get(targetClass, entityId);
    }

    public <T> boolean exist(Class<T> targetClass, long entityId) {
        Session session = entityManager.unwrap(Session.class);
        Criteria criteria = session.createCriteria(targetClass);
        criteria.add(Restrictions.eq("id", entityId));
        criteria.setProjection(Projections.rowCount());

        List<Long> results = criteria.list();

        return results.get(0) >= 1;
    }

}
