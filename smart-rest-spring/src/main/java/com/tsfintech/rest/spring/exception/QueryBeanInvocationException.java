package com.tsfintech.rest.spring.exception;

/**
 * Created by jack on 14-12-15.
 */
public class QueryBeanInvocationException extends RuntimeException {
    public QueryBeanInvocationException(String daoName, String methodName, Throwable cause) {
        super("Can not invoke dao:" + daoName + "." + methodName, cause);
    }

    public QueryBeanInvocationException(String daoName, String methodName) {
        super("Can not invoke dao:" + daoName + "." + methodName + ", caused by: no such method.");
    }
}
