package me.tofaa.rsl.interpreter.runtime;

import me.tofaa.rsl.exception.RSLInterpretException;

import java.util.List;

public interface JavaProxiedRuntimeValue extends RuntimeValue {

    static List<RuntimeValue> wrapPrimaryArray(List<Object> objs) {
        return objs.stream().map(JavaProxiedRuntimeValue::wrapPrimary).toList();
    }

    public static List<Object> wrapInterpretedArray(List<RuntimeValue> values) {
        return values.stream().map(JavaProxiedRuntimeValue::wrapInterpreted).toList();
    }

    static  RuntimeValue wrapPrimary(Object o) {
        return switch (o) {
            case Number n -> new NumberValue(n);
            case String s -> new StringValue(s);
            case Boolean b -> new BooleanValue(b);
            case null -> NullValue.INSTANCE;
            default -> new JavaObjectValue(o);
        };
    }

    static Object wrapInterpreted(RuntimeValue v) {
        if (v instanceof StringValue(String value) ) {
            return value;
        }
        else if (v instanceof NumberValue(Number value)) {
            return value;
        }
        else if (v instanceof NullValue) {
            return null;
        }
        else if (v instanceof BooleanValue(Boolean value)) {
            return value;
        } else if (v instanceof JavaObjectValue(Object val)) {
            return val;
        }
        throw new RSLInterpretException("Could not find suitable java representation for value " + v.toString());
    }

    static boolean isCompatibleType(Class<?> paramType, Class<?> argType) {
        if (paramType.isAssignableFrom(argType)) return true;

        // Handle primitive and wrapper class compatibility
        return (paramType == int.class && argType == Integer.class) ||
                (paramType == double.class && argType == Double.class) ||
                (paramType == float.class && argType == Float.class) ||
                (paramType == long.class && argType == Long.class) ||
                (paramType == boolean.class && argType == Boolean.class) ||
                (paramType == char.class && argType == Character.class) ||
                (paramType == byte.class && argType == Byte.class) ||
                (paramType == short.class && argType == Short.class);
    }

}

