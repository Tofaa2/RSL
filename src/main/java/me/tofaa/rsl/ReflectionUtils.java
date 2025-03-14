package me.tofaa.rsl;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.IntFunction;

/**
 * Not the greatest class ever made, but it'll suffice.
 * The lookup methods use getField and getMethods instead of the declared field and methods because we only need public stuff
 */
public final class ReflectionUtils {

    private static final Map<Class<?>, Map<String, Field>> fieldLookup = new HashMap<>();
    private static final Map<Class<?>, Map<String, Method>> methodLookup = new HashMap<>();
    private static final Map<Class<?>, Enum<?>[]> enumLookup = new HashMap<>();
    private ReflectionUtils() {}


    public static Object callStaticMethod(Class<?> clazz, String name, Object[] params) {
        return callMethod(true, clazz, name, null, params);
    }

    public static Object callMemberMethod(Class<?> clazz, String name, Object instance, Object[] params) {
        return callMethod(false, clazz, name, instance, params);
    }

    private static Object callMethod(boolean isStatic, Class<?> clazz, String name, Object instance, Object... params) {
        Class<?>[] paramsClasses = Arrays.stream(params).map(Object::getClass).toArray((IntFunction<Class<?>[]>) Class[]::new);
        Method m = lookupMethod(clazz, name, paramsClasses);
        try {
            return m.invoke(isStatic ? null : instance, params);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    public static Object getStaticField(Class<?> clazz, String name) {
        return getField(true, clazz, name, null);
    }

    public static void setStaticField(Class<?> clazz, String name, Object value) {
        setField(true, clazz, name, null, value);
    }

    public static Object getMemberField(Class<?> clazz, String name,  Object instance) {
        return getField(false, clazz, name, instance);
    }

    public static void setMemberField(Class<?> clazz, String name, Object instance, Object value) {
        setField(false, clazz, name, instance, value);
    }

    private static void setField(boolean isStatic, Class<?> clazz, String name, Object instance, Object value) {
        Field f = lookupField(clazz, name);
        try {
            f.set(isStatic ? null : instance, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static Object getField(boolean isStatic, Class<?> clazz, String name, Object instance) {
        Field f = lookupField(clazz, name);
        try {
            return f.get(isStatic ? null : instance);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Method lookupMethod(Class<?> clazz, String name, Class<?>... params) {
        var lookup = methodLookup.computeIfAbsent(clazz, (v) -> new HashMap<>());
        Method m;
        if (!lookup.containsKey(name)) {
            try {
                m = clazz.getMethod(name, params);
                m.setAccessible(true);
            }  catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
            lookup.put(name, m);
        }
        else {
            m = lookup.get(name);
        }
        return m;
    }

    public static Field lookupField(Class<?> clazz, String name) {
        var lookup = fieldLookup.computeIfAbsent(clazz, (v) -> new HashMap<>());
        Field f;
        if (!lookup.containsKey(name)) {
            try {
                f = clazz.getField(name);
                f.setAccessible(true);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
            lookup.put(name, f);
        }
        else {
            f = lookup.get(name);
        }
        return f;
    }

    public static Enum<?> getEnumMember(Class<?> clazz, String name) {
        if (enumLookup.containsKey(clazz)) {
            return Arrays.stream(enumLookup.get(clazz)).filter(f -> f.name().equals(name)).findFirst().orElseThrow();
        }
        Enum<?>[] enums = (Enum<?>[]) clazz.getEnumConstants();
        enumLookup.put(clazz, enums);
        return Arrays.stream(enums).filter(f -> f.name().equals(name)).findFirst().orElseThrow();
    }


}
