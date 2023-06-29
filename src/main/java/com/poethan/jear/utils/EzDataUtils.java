package com.poethan.jear.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EzDataUtils {
    public static boolean isCollectionNoKey(Object o) {
        return o instanceof List || o instanceof Set;
    }

    public static boolean isSimpleCollectionNoKey(Object o) {
        if (!isCollectionNoKey(o)) {
            return false;
        }
        if (o instanceof List) {
            for (Object l:(List<Object>)o) {
                if (!isScalar(l)) {
                    return false;
                }
            }
        }

        if (o instanceof Set) {
            for (Object l:(Set<Object>)o) {
                if (!isScalar(l)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isScalar(Object o) {
        return o instanceof String || o instanceof Long || o instanceof Integer || o instanceof Float || o instanceof Double;
    }

    public static <T> T convertValue(Object o, Class<T> tClass) {
        if (Objects.isNull(o)) {
            return null;
        }
        if (tClass.isAssignableFrom(o.getClass())) {
            return (T) o;
        } else if (String.class.isAssignableFrom(tClass)) {
            return tClass.cast(o.toString());
        } else if (Long.class.isAssignableFrom(tClass)) {
            return tClass.cast(Long.parseLong(o.toString()));
        } else if (Integer.class.isAssignableFrom(tClass)) {
            return tClass.cast(Integer.parseInt(o.toString()));
        } else {
            return (T) o;
        }
    }

    public static boolean isNumberValue(Object v) {
        if (Objects.isNull(v)) {
            return false;
        }
        try {
            Long.parseLong(v.toString());
            return true;
        }catch (NumberFormatException nfe) {
            return false;
        }
    }
}
