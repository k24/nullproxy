package com.github.k24.nullproxy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by k24 on 2017/01/12.
 */
public final class ObjectUtil {

    private ObjectUtil() {
        //no instance
    }

    @Nonnull
    public static String classToName(@Nonnull Object object) {
        Class<?> objectClass = object.getClass();
        return classToName(objectClass);
    }

    @Nonnull
    public static String classToName(@Nonnull Object object, Pattern patternToExclude) {
        Class<?> objectClass = object.getClass();
        return classToName(objectClass, patternToExclude);
    }

    @Nonnull
    public static String classToName(@Nonnull Class<?> objectClass) {
        return classToName(objectClass, Patterns.NO_CLASS);
    }

    @Nonnull
    public static String classToName(@Nonnull Class<?> objectClass, Pattern patternToExclude) {
        while (patternToExclude.matcher(objectClass.getSimpleName()).matches()) {
            objectClass = objectClass.getSuperclass();
        }
        return objectClass.getCanonicalName();
    }

    public static boolean isNull(@Nullable Object object) {
        return object == null || matchNull(object);
    }

    private static boolean matchNull(Object object) {
        Class<?> objectClass = object.getClass();
        do {
            if (getNull(objectClass) == object) return true;
            for (Class<?> interfaceClass : objectClass.getInterfaces()) {
                if (getNull(interfaceClass) == object) return true;
            }
            objectClass = objectClass.getSuperclass();
        } while (objectClass != null);
        return false;
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public static <T> T getNull(@Nonnull Class<?> clazz) {
        try {
            if (clazz.isPrimitive()) {
                return (T) getPrimitiveNull(clazz);
            }
            return (T) clazz.getDeclaredField("NULL").get(null);
        } catch (Exception e) {
            return null;
        }
    }

    static Object getPrimitiveNull(Class<?> clazz) {
        return ValueHolder.PRIMITIVE_NULLS.get(clazz);
    }

    private static class ValueHolder {
        private static final Map<Class<?>, Object> PRIMITIVE_NULLS;

        static {
            HashMap<Class<?>, Object> map = new HashMap<>();
            map.put(boolean.class, false);
            map.put(byte.class, (byte) 0);
            map.put(char.class, (char) 0);
            map.put(short.class, (short) 0);
            map.put(int.class, 0);
            map.put(long.class, 0L);
            map.put(float.class, 0f);
            map.put(double.class, 0.0);
            PRIMITIVE_NULLS = map;
        }
    }

    public static class Patterns {
        public static final Pattern NO_CLASS = Pattern.compile("");
        public static final Pattern WELL_KNOWN_GENERATED_CLASS =
                Pattern.compile("(^[$]?AutoValue_.+|.+_$)");
    }
}