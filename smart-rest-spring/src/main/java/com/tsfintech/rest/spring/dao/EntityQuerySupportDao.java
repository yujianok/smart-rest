package com.tsfintech.rest.spring.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.tsfintech.rest.core.model.EntityQuery;
import com.tsfintech.rest.core.model.QueryComparator;
import com.tsfintech.rest.core.model.QueryCondition;
import com.tsfintech.rest.core.util.StringValueUtil;


/**
 * Created by jack on 14-9-1.
 */
@Repository
@Transactional
public class EntityQuerySupportDao {

    @PersistenceContext
    private EntityManager entityManager;

    public <T> long count(Class<T> target, EntityQuery entityQuery) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);

        Root<T> targetRoot = criteria.from(target);
        criteria.select(builder.count(targetRoot));

        List<Predicate> predicates = new ArrayList<>();
        for (QueryCondition condition : entityQuery.getConditions()) {
            Predicate predicate = buildPredicate(builder, targetRoot, condition);
            predicates.add(predicate);
        }

        criteria.where(predicates.toArray(new Predicate[predicates.size()]));

        return entityManager.createQuery(criteria).getSingleResult();
    }

    public <T> List<T> find(Class<T> target, EntityQuery entityQuery) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<T> criteria = builder.createQuery(target);

        Root<T> targetRoot = criteria.from(target);
        criteria.select(targetRoot);

        List<Predicate> predicates = new ArrayList<>();
        for (QueryCondition condition : entityQuery.getConditions()) {
            Predicate predicate = buildPredicate(builder, targetRoot, condition);
            predicates.add(predicate);
        }

        criteria.where(predicates.toArray(new Predicate[predicates.size()]));

        if (entityQuery.getOrderBy() != null) {
            String[] orderBys = entityQuery.getOrderBy().split(",");
            List<Order> orders = new ArrayList<>();
            for (String orderBy : orderBys) {
                if (entityQuery.isAsc()) {
                    orders.add(builder.asc(targetRoot.get(orderBy.trim())));
                } else {
                    orders.add(builder.desc(targetRoot.get(orderBy.trim())));
                }
            }

            if (entityQuery.isAsc()) {
                criteria.orderBy(orders);
            } else {
                criteria.orderBy(orders);
            }
        }

        TypedQuery<T> typedQuery = entityManager.createQuery(criteria);
        if (entityQuery.isPaged()) {
            typedQuery.setFirstResult((entityQuery.getPageNo() - 1) * entityQuery.getPageSize());
            typedQuery.setMaxResults(entityQuery.getPageSize());
        }

        return typedQuery.getResultList();
    }

    public Object update(Object entity) {
        return entityManager.merge(entity);
    }

    public void save(Object entity) {
        entityManager.persist(entity);
    }

    public void delete(Object entity) {
        entityManager.remove(entity);
    }

    private Predicate buildPredicate(CriteriaBuilder criteriaBuilder, Root<?> targetRoot, QueryCondition condition) {
        String name = condition.getPropertyName();
        String stringValue = condition.getPropertyValue();
        Class<?> fieldType = targetRoot.get(name).getJavaType();
        Comparable<?> comparable = (Comparable<?>) StringValueUtil.getObjectValue(stringValue, fieldType);
        Path<Comparable> path = targetRoot.get(name);

        Predicate predicate = null;
        if (condition.getComparator() == QueryComparator.EQUAL) {
            if (stringValue == null) {
                predicate = criteriaBuilder.isNull(targetRoot.get(name));
            } else {
                predicate = criteriaBuilder.equal(path, comparable);
            }
        } else if (condition.getComparator() == QueryComparator.NOT_EQUAL) {
            if (stringValue == null) {
                predicate = criteriaBuilder.isNotNull(targetRoot.get(name));
            } else {
                predicate = criteriaBuilder.notEqual(path, comparable);
            }
        } else if (condition.getComparator() == QueryComparator.GREATER_EQUAL) {
            predicate = criteriaBuilder.greaterThanOrEqualTo(path, comparable);
        } else if (condition.getComparator() == QueryComparator.GREATER_THAN) {
            predicate = criteriaBuilder.greaterThan(path, comparable);
        } else if (condition.getComparator() == QueryComparator.LESS_EQUAL) {
            predicate = criteriaBuilder.lessThanOrEqualTo(path, comparable);
        } else if (condition.getComparator() == QueryComparator.LESS_THAN) {
            predicate = criteriaBuilder.lessThan(path, comparable);
        } else if (condition.getComparator() == QueryComparator.LIKE) {
            Path<String> stringPath = targetRoot.get(name);
            predicate = criteriaBuilder.like(stringPath, stringValue);
        }

        return predicate;
    }


}
