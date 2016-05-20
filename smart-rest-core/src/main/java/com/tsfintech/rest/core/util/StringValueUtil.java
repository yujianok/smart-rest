package com.tsfintech.rest.core.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;

/**
 * Created by jack on 14-12-15.
 */
public class StringValueUtil {

    public static <T> T getObjectValue(String value, Class<T> javaType) {
        if (value == null) {
            return null;
        }

        Object realValue = null;
        if (javaType.equals(Long.class) || javaType.equals(long.class)) {
            realValue = Long.valueOf(value);
        } else if (javaType.equals(Integer.class) || javaType.equals(int.class)) {
            realValue = Integer.valueOf(value);
        } else if (javaType.equals(BigDecimal.class)) {
            realValue = new BigDecimal(value);
        } else if (javaType.equals(Boolean.class) || javaType.equals(boolean.class)) {
            realValue = Boolean.valueOf(value);
        } else if (javaType.equals(Date.class)) {
            realValue = new Date(Long.valueOf(value));
        } else if (javaType.equals(String.class)) {
            realValue = value;
        } else if (javaType.isEnum()) {
            for (Object obj : javaType.getEnumConstants()) {
                Enum e = (Enum) obj;
                if (e.name().equals(value)) {
                    realValue = e;
                    break;
                }
            }
        } else {
            throw new RuntimeException("Not supported type: " + javaType.getName());
        }

        return (T) realValue;
    }

    public static String getStringValue(Object value, Class<?> javaType) {
        if (value == null) {
            return null;
        }

        String stringValue = null;
        if (javaType.equals(Long.class) || javaType.equals(long.class)) {
            stringValue = Long.toString((Long) value);
        } else if (javaType.equals(Integer.class) || javaType.equals(int.class)) {
            stringValue = Integer.toString((Integer) value);
        } else if (javaType.equals(BigDecimal.class)) {
            stringValue = value.toString();
        } else if (javaType.equals(Boolean.class) || javaType.equals(boolean.class)) {
            stringValue = Boolean.toString((Boolean) value);
        } else if (javaType.equals(Date.class)) {
            stringValue = String.valueOf(((Date) value).getTime());
        } else if (javaType.equals(String.class)) {
            stringValue = (String) value;
        } else if (javaType.isEnum()) {
            Enum e = (Enum) value;
            stringValue = e.name();
        } else {
            throw new RuntimeException("Not supported type: " + javaType.getName());
        }

        return stringValue;
    }
}
