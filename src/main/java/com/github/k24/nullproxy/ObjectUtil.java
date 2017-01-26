package com.github.k24.nullproxy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
        return classToName(objectClass, PatternHolder.ANY_CLASS);
    }

    @Nonnull
    public static String classToName(@Nonnull Class<?> objectClass, Pattern patternToExclude) {
        while (patternToExclude.matcher(objectClass.getSimpleName()).matches()) {
            objectClass = objectClass.getSuperclass();
        }
        return objectClass.getName();
    }

    public static boolean isNull(@Nullable Object object) {
        return object == null || getNull(object.getClass()) == object;
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public static <T> T getNull(@Nonnull Class<?> clazz) {
        try {
            return (T) clazz.getDeclaredField("NULL").get(null);
        } catch (Exception e) {
            return null;
        }
    }

    public static class PatternHolder {
        public static final Pattern ANY_CLASS = Pattern.compile(".+");
        public static final Pattern WELL_KNOWN_GENERATED_CLASS =
                Pattern.compile("(^[$]?AutoValue_.+|.+_$)");
    }
}