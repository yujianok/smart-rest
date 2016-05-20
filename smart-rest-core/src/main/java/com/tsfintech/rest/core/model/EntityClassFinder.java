package com.tsfintech.rest.core.model;

/**
 * Created by jack on 15-1-13.
 */
public interface EntityClassFinder {

    public Class<?> findClass(EntityUri entityUri);

}
