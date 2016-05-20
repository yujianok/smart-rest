package com.tsfintech.rest.example.util;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.util.StringUtils;

import com.tsfintech.rest.core.model.EntityQuery;
import com.tsfintech.rest.core.model.QueryComparator;
import com.tsfintech.rest.core.model.QueryCondition;

/**
 * Created by jack on 14-9-1.
 */
public class QueryConverter {

    public static DetachedCriteria convertToCriteria(Class<?> entityClass, EntityQuery query) {
        DetachedCriteria criteria = DetachedCriteria.forClass(entityClass);
        List<QueryCondition> conditions = query.getConditions();
        for (QueryCondition condition : conditions) {
            QueryComparator comparator = condition.getComparator();
            String name = condition.getPropertyName();
            String valueStr = condition.getPropertyValue();
            Object value = getValue(entityClass, name, valueStr);
            if (QueryComparator.EQUAL == comparator) {
                if (value == null) {
                    criteria.add(Restrictions.isNull(name));
                } else {
                    criteria.add(Restrictions.eq(name, value));
                }
            } else if (QueryComparator.NOT_EQUAL == comparator) {
                if (value == null) {
                    criteria.add(Restrictions.isNotNull(name));
                } else {
                    criteria.add(Restrictions.ne(name, value));
                }
            } else if (QueryComparator.GREATER_THAN == comparator) {
                criteria.add(Restrictions.gt(name, value));
            } else if (QueryComparator.GREATER_EQUAL == comparator) {
                criteria.add(Restrictions.ge(name, value));
            } else if (QueryComparator.LESS_THAN == comparator) {
                criteria.add(Restrictions.lt(name, value));
            } else if (QueryComparator.LESS_EQUAL == comparator) {
                criteria.add(Restrictions.le(name, value));
            } else if (QueryComparator.LIKE == comparator) {
                criteria.add(Restrictions.like(name, value.toString(), MatchMode.ANYWHERE));
            } else {
                throw new UnsupportedOperationException("Not supported query comparator: " + comparator);
            }
        }

        if (!StringUtils.isEmpty(query.getOrderBy())) {
            if (query.isAsc()) {
                criteria = criteria.addOrder(Order.asc(query.getOrderBy()));
            } else {
                criteria = criteria.addOrder(Order.desc(query.getOrderBy()));
            }
        }
        return criteria;
    }

    private static Object getValue(Class<?> entityType, String name, String valueStr) {
        Field field;
        try {
            field = entityType.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            try {
                field = entityType.getSuperclass().getDeclaredField(name);
            } catch (NoSuchFieldException e1) {
                throw new RuntimeException(e1);
            }
        }
        Class<?> fieldType = field.getType();
        Object value = null;
        if (valueStr != null) {
            if (fieldType.equals(Long.class) || fieldType.equals(long.class)) {
                value = Long.valueOf(valueStr);
            } else if (fieldType.equals(Integer.class) || fieldType.equals(int.class)) {
                value = Integer.valueOf(valueStr);
            } else if (fieldType.equals(BigDecimal.class)) {
                value = new BigDecimal(valueStr);
            } else if (fieldType.equals(Boolean.class) || fieldType.equals(boolean.class)) {
                value = Boolean.valueOf(valueStr);
            } else if (fieldType.equals(Date.class)) {
                value = new Date(Long.valueOf(valueStr));
            } else if (fieldType.equals(String.class)) {
                value = valueStr;
            } else if (fieldType.isEnum()) {
                for (Object obj : fieldType.getEnumConstants()) {
                    Enum e = (Enum) obj;
                    if (e.name().equals(valueStr)) {
                        value = e;
                        break;
                    }
                }
            } else {
                throw new RuntimeException("Not supported type: " + fieldType.getName());
            }
        }
        return value;
    }

}
