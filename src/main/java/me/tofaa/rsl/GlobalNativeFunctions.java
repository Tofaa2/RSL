package me.tofaa.rsl;

import me.tofaa.rsl.environment.Environment;
import me.tofaa.rsl.interpreter.runtime.*;

import java.util.StringJoiner;
import java.util.stream.Collectors;

public final class GlobalNativeFunctions {

    private GlobalNativeFunctions() {}

    public static NativeFunctionValue PRINT_LN = new NativeFunctionValue((environment, arguments) ->{
        for (var arg : arguments) {
            if (arg instanceof NumberValue(Number value)) {
                System.out.println(value);
            }
            else if (arg instanceof BooleanValue(Boolean b)) {
                System.out.println(b);
            }
            else if (arg instanceof FunctionValue fn) {
                System.out.printf(
                        "Function %s with args %s%n", fn.name(),
                        String.join(", ", fn.params())
                );
            }
            else if (arg instanceof NullValue) {
                System.out.println("null");
            }
            else if (arg instanceof ObjectValue b) {
                String mapAsString = b.properties().keySet().stream()
                        .map(key -> key + "=" + b.properties().get(key))
                        .collect(Collectors.joining(", ", "{", "}"));
                System.out.println(mapAsString);
            }
            else {
                System.out.println(arg);
            }
        }
        return NullValue.INSTANCE;
    });

    public static NativeFunctionValue CURRENT_TIME_MS = new NativeFunctionValue(((environment, arguments) -> new NumberValue(System.currentTimeMillis())));

    public static void register(
            Environment env
    ) {
        env.declare("println", PRINT_LN, true);
        env.declare("currentTimeMs", CURRENT_TIME_MS, true);
    }

}
