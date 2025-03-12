package me.tofaa.rsl;

import me.tofaa.rsl.environment.Environment;
import me.tofaa.rsl.interpreter.runtime.NativeFunctionValue;
import me.tofaa.rsl.interpreter.runtime.NullValue;
import me.tofaa.rsl.interpreter.runtime.NumberValue;

public final class GlobalNativeFunctions {

    private GlobalNativeFunctions() {}

    public static NativeFunctionValue PRINT_LN = new NativeFunctionValue((environment, arguments) ->{
        for (var arg : arguments) {
            System.out.println(arg);
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
