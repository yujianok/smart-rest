package com.tsfintech.rest.core.meta;

/**
 * Created by jack on 15-1-27.
 */
public class RestMetaUtil {

    public static String getPrimaryKey(Class<?> type) {
        RestEntity restEntity = type.getAnnotation(RestEntity.class);

        return restEntity.entityId();
    }

    public static String getScopeKey(Class<?> type) {
        RestEntity restEntity = type.getAnnotation(RestEntity.class);

        return restEntity.entityScope();
    }

    public static boolean defaultFind(Class<?> type) {
        RestEntity restEntity = type.getAnnotation(RestEntity.class);

        return restEntity.defaultFind();
    }

    public static boolean defaultRetrieve(Class<?> type) {
        RestEntity restEntity = type.getAnnotation(RestEntity.class);

        return restEntity.defaultRetrieve();
    }

    public static boolean defaultSave(Class<?> type) {
        RestEntity restEntity = type.getAnnotation(RestEntity.class);

        return restEntity.defaultSave();
    }

    public static boolean defaultDelete(Class<?> type) {
        RestEntity restEntity = type.getAnnotation(RestEntity.class);

        return restEntity.defaultDelete();
    }

    public static boolean defaultCount(Class<?> type) {
        RestEntity restEntity = type.getAnnotation(RestEntity.class);

        return restEntity.defaultCount();
    }

    public static boolean defaultUpdate(Class<?> type) {
        RestEntity restEntity = type.getAnnotation(RestEntity.class);

        return restEntity.defaultUpdate();
    }
}
