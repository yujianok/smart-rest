package com.tsfintech.rest.spring.delegate;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import com.tsfintech.rest.core.delegate.QueryBeanDelegate;
import com.tsfintech.rest.core.model.EntityQuery;
import com.tsfintech.rest.core.util.StringValueUtil;
import com.tsfintech.rest.spring.exception.QueryBeanInvocationException;

/**
 * Created by jack on 15-1-12.
 */
public class SpringQueryBeanDelegate implements QueryBeanDelegate {

    @Override
    public boolean hasSpecificMethod(Object bean, String methodName, EntityQuery entityQuery) {
        Class<?> targetClass = AopUtils.isAopProxy(bean) && !(bean instanceof Repository)
                ? AopUtils.getTargetClass(bean)
                : bean.getClass();

        Method specificMethod = getSpecificMethod(methodName, targetClass,
                entityQuery.isPaged(),
                entityQuery.getOrderBy() != null);

        return specificMethod != null;
    }

    @Override
    public <T> T invokeSpecificMethod(Object bean, String methodName, EntityQuery entityQuery) {
        Class<?> targetClass = AopUtils.isAopProxy(bean) && !(bean instanceof Repository)
                ? AopUtils.getTargetClass(bean)
                : bean.getClass();

        Method specificMethod = getSpecificMethod(methodName,
                targetClass,
                entityQuery.isPaged(),
                entityQuery.getOrderBy() != null);

        if (specificMethod == null) {
            throw new QueryBeanInvocationException(targetClass.getSimpleName(), methodName);
        }

        try {
            Object[] args = getArguments(entityQuery, specificMethod);
            return (T) specificMethod.invoke(bean, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new QueryBeanInvocationException(targetClass.getSimpleName(), methodName, e);
        }
    }

    private Object[] getArguments(EntityQuery entityQuery, Method specificMethod) {
        Annotation[][] annotations = specificMethod.getParameterAnnotations();
        Class<?>[] parameterTypes = specificMethod.getParameterTypes();
        Object[] args = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> parameterType = parameterTypes[i];
            if (Pageable.class.isAssignableFrom(parameterType)) {
                if (entityQuery.getOrderBy() != null) {
                    String[] orderBys = entityQuery.getOrderBy().replace(" ", "").split(",");
                    Sort sort = new Sort(entityQuery.isAsc() ? Sort.Direction.ASC : Sort.Direction.DESC, orderBys);
                    args[i] = new PageRequest(entityQuery.getPageNo() - 1, entityQuery.getPageSize(), sort);
                } else {
                    args[i] = new PageRequest(entityQuery.getPageNo() - 1, entityQuery.getPageSize());
                }
            } else if (Sort.class.isAssignableFrom(parameterType)) {
                String[] orderBys = entityQuery.getOrderBy().replace(" ", "").split(",");

                args[i] = new Sort(entityQuery.isAsc() ? Sort.Direction.ASC : Sort.Direction.DESC, orderBys);
            } else {
                for (Annotation annotation : annotations[i]) {
                    if (annotation instanceof Param) {
                        String conditionName = ((Param) annotation).value();
                        String stringValue = entityQuery.getConditionValue(conditionName);
                        args[i] = StringValueUtil.getObjectValue(stringValue, parameterType);
                    }
                }
            }
        }

        return args;
    }

    private Method getSpecificMethod(String methodName, Class<?> clazz, boolean paged, boolean order) {
        if (clazz.isInterface()) {
            for (Method method : clazz.getMethods()) {
                if (method.getName().equals(methodName) && suitable(method, paged, order)) {
                    return method;
                }
            }
        } else {
            for (Class<?> interfaceClass : clazz.getInterfaces()) {
                Method method = getSpecificMethod(methodName, interfaceClass, paged, order);
                if (method != null) {
                    return method;
                }
            }

            for (Method method : clazz.getMethods()) {
                if (method.getName().equals(methodName)) {
                    if (method.getName().equals(methodName) && suitable(method, paged, order)) {
                        return method;
                    }
                }
            }
        }

        return null;
    }

    private boolean suitable(Method method, boolean paged, boolean order) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (paged && order) {
            return ArrayUtils.contains(parameterTypes, Pageable.class);
        } else if (paged) {
            return ArrayUtils.contains(parameterTypes, Pageable.class);
        } else if (order) {
            return !ArrayUtils.contains(parameterTypes, Pageable.class)
                    && ArrayUtils.contains(parameterTypes, Sort.class);
        } else {
            return !ArrayUtils.contains(parameterTypes, Pageable.class)
                    && !ArrayUtils.contains(parameterTypes, Sort.class);
        }

    }

}
