package me.tofaa.rsl;

import me.tofaa.rsl.exception.RSLInterpretException;
import me.tofaa.rsl.interpreter.runtime.*;

import java.util.HashMap;
import java.util.Map;

import static me.tofaa.rsl.Utils.entry;

public final class GlobalNativeFunctions {

    private GlobalNativeFunctions() {}

    public static ObjectValue RSL_CONSTANT = makeObj(Map.ofEntries(
            entry("println", makeNativeFn((env, args) -> {
                args.forEach(arg -> System.out.println(arg.toString())); return NullValue.INSTANCE;
            })),
            entry("javaClass", makeNativeFn((env, args) -> {
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
            })),
            entry("toString", makeNativeFn((env, args) -> {
                if (args.size() != 1) {
                    return NullValue.INSTANCE;
                }
                return new StringValue(args.getFirst().toString());
            })),
            entry("typeOf", makeNativeFn((env, args) -> {
                if (args.size() != 1) {
                    return NullValue.INSTANCE;
                }
                return new StringValue(args.getFirst().getClass().getSimpleName());
            })),
            entry("timeNs", makeNativeFn((env, args) -> new NumberValue(System.nanoTime()))),
            entry("timeMs", makeNativeFn((env, args) -> new NumberValue(System.currentTimeMillis())))
    ));

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
        //env.declare("RSL", RSL_CONSTANT, true);
    }

    public static NativeFunctionValue makeNativeFn(RSLFunction func) {
        return new NativeFunctionValue(func);
    }

    public static ObjectValue makeObj(Map<String, RuntimeValue> data) {
        return new ObjectValue(data);
    }

}
