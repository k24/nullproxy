package com.github.k24.nullproxy;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by k24 on 2017/01/12.
 */
public final class NullProxy implements InvocationHandler {
    private NullProxy() {
        //no instance
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public static <T> T newProxyInstance(@Nonnull Class<T> interfaceClass) {
        if (!interfaceClass.isInterface()) throw new IllegalArgumentException("Only for interface");
        synchronized (ValueHolder.OTHERS) {
            Object value = ValueHolder.OTHERS.get(interfaceClass);
            if (value != null) return (T) value;

            T nullInstance = (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(),
                    new Class[]{interfaceClass},
                    new NullProxy());

            // Check again after class loader
            value = ValueHolder.OTHERS.get(interfaceClass);
            if (value != null) return (T) value;

            ValueHolder.OTHERS.put(interfaceClass, nullInstance);
            return nullInstance;
        }
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        Class<?> returnType = method.getReturnType();
        return ValueHolder.get(returnType);
    }

    public static void clearCache() {
        ValueHolder.clear();
    }

    private static class ValueHolder {
        private static final Map<Class<?>, Object> PRIMITIVE_NULLS;
        private static final Map<Class<?>, Object> OTHERS = new HashMap<>();

        static {
            HashMap<Class<?>, Object> map = new HashMap<>();
            map.put(boolean.class, false);
            map.put(char.class, (char) 0);
            map.put(short.class, (short) 0);
            map.put(int.class, 0);
            map.put(long.class, 0L);
            map.put(float.class, 0f);
            map.put(double.class, 0.0);
            PRIMITIVE_NULLS = map;
        }

        public static Object get(Class<?> clazz) {
            Object value = PRIMITIVE_NULLS.get(clazz);
            if (value != null) return value;
            synchronized (OTHERS) {
                value = OTHERS.get(clazz);
                if (value != null) return value;
                value = ObjectUtil.getNull(clazz);
                if (value != null) {
                    OTHERS.put(clazz, value);
                } else {
                    if (clazz.isInterface()) {
                        value = NullProxy.newProxyInstance(clazz);
                        OTHERS.put(clazz, value);
                    }
                }
                return value;
            }
        }

        public static void clear() {
            synchronized (OTHERS) {
                OTHERS.clear();
            }
        }
    }
}
