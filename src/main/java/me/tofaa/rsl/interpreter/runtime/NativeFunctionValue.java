package me.tofaa.rsl.interpreter.runtime;

import me.tofaa.rsl.environment.Environment;
import me.tofaa.rsl.interpreter.RSLInterpreterValueTypes;

import java.util.List;

import static me.tofaa.rsl.interpreter.RSLInterpreterValueTypes.NATIVE_FUNCTION;

public record NativeFunctionValue(
        RSLFunction call
) implements RuntimeValue{
    @Override
    public RSLInterpreterValueTypes type() {
        return NATIVE_FUNCTION;
    }


}
