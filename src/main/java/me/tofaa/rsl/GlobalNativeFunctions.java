package me.tofaa.rsl;

import me.tofaa.rsl.exception.RSLInterpretException;
import me.tofaa.rsl.interpreter.runtime.*;

import java.util.HashMap;
import java.util.Map;

import static me.tofaa.rsl.Utils.entry;

public final class GlobalNativeFunctions {

    private GlobalNativeFunctions() {}


    public static void register(
            Environment env
    ) {
        env.declare("typeOf", makeNativeFn((env1, args) -> {
                    if (args.size() != 1) {
                        return NullValue.INSTANCE;
                    }
                    return new StringValue(args.getFirst().getClass().getSimpleName());
                }), true);
        env.declare("print", makeNativeFn((env1, args) -> {
                    args.forEach(arg -> System.out.println(arg.toString())); return NullValue.INSTANCE;
                }), true);
        env.declare("math", new JavaClassValue(Math.class), true);
        env.declare("toString", makeNativeFn((env1, args) -> {
            if (args.size() != 1) {
                return NullValue.INSTANCE;
            }
            return new StringValue(args.getFirst().toString());
        }), true);
        env.declare("javaClass", makeNativeFn((env1, args) -> {
            if (args.size() != 1 && !(args.getFirst() instanceof StringValue)) {
                throw new RSLInterpretException("Expected a string argument for class path in javaClass call");
            }
            String s = args.getFirst().asString().value();
            try {
                var clazz = Class.forName(s);
                return new JavaClassValue(clazz);
            } catch (ClassNotFoundException e) {
                throw new RSLInterpretException(e);
            }
        }), true);
        env.declare("javaObject", makeNativeFn((env1, args) -> {
            if (args.isEmpty()) {
                throw new RSLInterpretException("Expected a class or string + constructor arguments, got nothing.");
            }
            Object o = null;
            if (args.getFirst() instanceof JavaClassValue jc) {
                o = ReflectionUtils.newInstance(jc.clazz(), JavaProxiedRuntimeValue.wrapInterpretedArray(
                        args.subList(1, args.size())
                ).toArray());
            }
            else if (args.getFirst() instanceof StringValue s) {
                Class<?> clazz = ReflectionUtils.getClass(s.value());
                o = ReflectionUtils.newInstance(clazz, JavaProxiedRuntimeValue.wrapInterpretedArray(
                        args.subList(1, args.size())
                ).toArray());
            }
            if (o == null) {
                throw new RSLInterpretException("Failed to create or find a constructor for the class " + args.getFirst().toString());
            }
            return new JavaObjectValue(o);
        }), true);
    }

    public static NativeFunctionValue makeNativeFn(RSLFunction func) {
        return new NativeFunctionValue(func);
    }

    public static ObjectValue makeObj(Map<String, RuntimeValue> data) {
        return new ObjectValue(data);
    }

}
