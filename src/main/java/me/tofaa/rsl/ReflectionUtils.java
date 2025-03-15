package me.tofaa.rsl;

import me.tofaa.rsl.interpreter.runtime.NumberValue;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Not the greatest class ever made, but it'll suffice.
 * The lookup methods use getField and getMethods instead of the declared field and methods because we only need public stuff
 */
public final class ReflectionUtils {

    private static final Map<Class<?>, Map<String, Field>> fieldLookup = new HashMap<>();
    private static final Map<Class<?>, Map<String, Method>> methodLookup = new HashMap<>();
    private static final Map<Class<?>, Enum<?>[]> enumLookup = new HashMap<>();
    private ReflectionUtils() {}


    public static Object newInstance(Class<?> clazz, Object... args) {
        Class<?>[] paramTypes = Arrays.stream(args)
                .map(ReflectionUtils::inferType) // Infer parameter types
                .toArray(Class<?>[]::new);

        try {
            var constructor = clazz.getConstructor(paramTypes);
            Object[] convertedArgs = convertParameters(constructor.getParameterTypes(), args);
            return constructor.newInstance(convertedArgs);
        } catch (NoSuchMethodException e) {
            // Try finding a compatible constructor if exact match fails
            for (var ctor : clazz.getConstructors()) {
                if (isCompatible(ctor.getParameterTypes(), paramTypes)) {
                    try {
                        Object[] convertedArgs = convertParameters(ctor.getParameterTypes(), args);
                        return ctor.newInstance(convertedArgs);
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
                        throw new RuntimeException("Failed to instantiate " + clazz.getName(), ex);
                    }
                }
            }
            throw new RuntimeException("No matching constructor found for " + clazz.getName(), e);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Failed to instantiate " + clazz.getName(), e);
        }
    }

    public static Class<?> getClass(String s) {
        try {
            return Class.forName(s);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object callStaticMethod(Class<?> clazz, String name, Object[] params) {
        return callMethod(true, clazz, name, null, params);
    }

    public static Object callMemberMethod(Class<?> clazz, String name, Object instance, Object[] params) {
        return callMethod(false, clazz, name, instance, params);
    }

    private static Object callMethod(boolean isStatic, Class<?> clazz, String name, Object instance, Object... params) {
        Class<?>[] paramTypes = Arrays.stream(params)
                .map(ReflectionUtils::inferType)
                .toArray(Class<?>[]::new);

        try {
            Method method = lookupMethod(clazz, name, paramTypes);
            Object[] convertedParams = convertParameters(method.getParameterTypes(), params);
            try {
                return method.invoke(isStatic ? null : instance, convertedParams);
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Class<?> inferType(Object value) {
        return switch (value) {
            case null -> Object.class; // Fallback type
            case Integer i -> int.class;
            case Double v -> double.class;
            case Float v -> float.class;
            case Long l -> long.class;
            case Short i -> short.class;
            case Byte b -> byte.class;
            case Boolean b -> boolean.class;
            case Character c -> char.class;
            default -> value.getClass();
        };

    }

    private static Object[] convertParameters(Class<?>[] expectedTypes, Object[] providedValues) {
        Object[] converted = new Object[providedValues.length];

        for (int i = 0; i < providedValues.length; i++) {
            converted[i] = convertToType(providedValues[i], expectedTypes[i]);
        }
        return converted;
    }

    private static Object convertToType(Object value, Class<?> targetType) {
        if (value == null) return null;

        if (targetType.isInstance(value)) {
            return value; // Already correct type
        }

        if (targetType.isPrimitive()) {
            if (value instanceof Number num) {
                if (targetType == double.class) return num.doubleValue();
                if (targetType == float.class) return num.floatValue();
                if (targetType == long.class) return num.longValue();
                if (targetType == int.class) return num.intValue();
                if (targetType == short.class) return num.shortValue();
                if (targetType == byte.class) return num.byteValue();
            } else if (targetType == boolean.class && value instanceof Boolean) {
                return value;
            } else if (targetType == char.class && value instanceof Character) {
                return value;
            }
        }

        throw new IllegalArgumentException("Cannot convert " + value.getClass() + " to " + targetType);
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

    public static Method lookupMethod(Class<?> clazz, String name, Class<?>... params) {
        var lookup = methodLookup.computeIfAbsent(clazz, (v) -> new HashMap<>());

        try {
            Method exactMatch = clazz.getMethod(name, params);
            exactMatch.setAccessible(true);
            lookup.put(name, exactMatch);
            return exactMatch;
        } catch (NoSuchMethodException ignored) {}

        Method bestMatch = null;
        for (Method method : clazz.getMethods()) {
            if (!method.getName().equals(name)) continue;

            Class<?>[] methodParams = method.getParameterTypes();
            if (isCompatible(methodParams, params)) {
                method.setAccessible(true);
                lookup.put(name, method);
                return method;
            }

            bestMatch = method;
        }

        if (bestMatch != null) {
            return bestMatch;
        }

        throw new RuntimeException("No matching method '" + name + "' found in " + clazz.getName());
    }

    private static boolean isCompatible(Class<?>[] expected, Class<?>[] given) {
        if (expected.length != given.length) return false;
        for (int i = 0; i < expected.length; i++) {
            if (!isAssignable(given[i], expected[i]) && !isBoxedPrimitiveMatch(expected[i], given[i])) {
                return false;
            }
        }
        return true;
    }

    private static boolean isAssignable(Class<?> from, Class<?> to) {
        if (from == NumberValue.class) {
            return isAssignable(Number.class, to);
        }

        if (to.isAssignableFrom(from)) return true;

        from = unbox(from);
        to = unbox(to);

        return (from == int.class && (to == long.class || to == float.class || to == double.class))
                || (from == long.class && (to == float.class || to == double.class))
                || (from == float.class && to == double.class)
                || (from == short.class && (to == int.class || to == long.class || to == float.class || to == double.class))
                || (from == byte.class && (to == short.class || to == int.class || to == long.class || to == float.class || to == double.class));
    }

    private static boolean isBoxedPrimitiveMatch(Class<?> expected, Class<?> given) {
        return (expected == double.class && given == Double.class)
                || (expected == int.class && given == Integer.class)
                || (expected == long.class && given == Long.class)
                || (expected == float.class && given == Float.class)
                || (expected == boolean.class && given == Boolean.class)
                || (expected == char.class && given == Character.class)
                || (expected == byte.class && given == Byte.class)
                || (expected == short.class && given == Short.class);
    }

    private static Class<?> unbox(Class<?> type) {
        if (type == Integer.class) return int.class;
        if (type == Long.class) return long.class;
        if (type == Double.class) return double.class;
        if (type == Float.class) return float.class;
        if (type == Short.class) return short.class;
        if (type == Byte.class) return byte.class;
        if (type == Boolean.class) return boolean.class;
        if (type == Character.class) return char.class;
        return type;
    }


    private static Object getField(boolean isStatic, Class<?> clazz, String name, Object instance) {
        var f = lookupFieldOrEnum(clazz, name);
        if (f instanceof Field field) {
            try {
                return field.get(isStatic ? null : instance);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            return f;
        }
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

    public static Object lookupFieldOrEnum(Class<?> clazz, String name) {
        try {
            return lookupField(clazz, name);
        }
        catch (Exception e) {
            return getEnumMember(clazz, name);
        }
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