package me.tofaa.rsl.interpreter.runtime;

import me.tofaa.rsl.interpreter.RSLInterpreterValueTypes;

public record NativeObjectValue(Object value) implements RuntimeValue {
    @Override
    public RSLInterpreterValueTypes type() {
        return RSLInterpreterValueTypes.NATIVE_OBJECT;
    }
}
