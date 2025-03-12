package me.tofaa.rsl;

import me.tofaa.rsl.environment.Environment;
import me.tofaa.rsl.interpreter.runtime.NativeFunctionValue;
import me.tofaa.rsl.interpreter.runtime.NullValue;

public final class GlobalNativeFunctions {

    private GlobalNativeFunctions() {}

    public static NativeFunctionValue PRINT_LN = new NativeFunctionValue((environment, arguments) ->{
        for (var arg : arguments) {
            System.out.println(arg);
        }
        return NullValue.INSTANCE;
    });

    public static void register(
            Environment env
    ) {
        env.declare("println", PRINT_LN, true);
    }

}
