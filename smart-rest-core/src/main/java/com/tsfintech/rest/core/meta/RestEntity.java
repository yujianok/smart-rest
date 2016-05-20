package com.tsfintech.rest.core.meta;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by jack on 16-5-18.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RestEntity {

    String entityId() default "id";

    String entityScope() default "userId";

    boolean defaultSave() default true;

    boolean defaultUpdate() default true;

    boolean defaultDelete() default false;

    boolean defaultRetrieve() default true;

    boolean defaultFind() default false;

    boolean defaultCount() default false;

}
