package com.tsfintech.rest.core.util;

import java.lang.reflect.Method;

import com.tsfintech.rest.core.operator.EntityInvoker;

/**
 * Created by jack on 15-4-30.
 */
public class OperationHelper {

    public static Method getInvokerMethod(String invokerMethod, EntityInvoker<?> entityInvoker, String[] args) {

        Method operationMethod = null;
        for (Method method : entityInvoker.getClass().getMethods()) {
            if (method.getName().equals(invokerMethod)
                    && method.getParameterTypes().length == args.length + 1) {
                operationMethod = method;
                break;
            }
        }

        return operationMethod;
    }

    public static Object[] buildArguments(Method operationMethod, Object entity, String[] args) {

        Class[] types = operationMethod.getParameterTypes();
        Object[] results = new Object[types.length];
        results[0] = entity;
        for (int i = 1; i < types.length; i++) {
            String stringValue = args[i - 1];
            results[i] = StringValueUtil.getObjectValue(stringValue, types[i]);
        }

        return results;
    }
}
