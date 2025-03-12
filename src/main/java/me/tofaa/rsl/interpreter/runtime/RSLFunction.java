package me.tofaa.rsl.interpreter.runtime;

import me.tofaa.rsl.environment.Environment;

import java.util.List;

@FunctionalInterface
public interface RSLFunction {

    /**
     * Represents a callable function.
     * @param environment the environment (aka scope) of the function. Most of the time this represents the global scope of the script/interpreter
     * @param arguments the arguments passed into the function.
     * @return the value returned. If the function returns void, it will return a {@link NullValue}
     */
    RuntimeValue call(Environment environment, List<RuntimeValue> arguments);

}
