package me.tofaa.rsl.interpreter.runtime;

import me.tofaa.rsl.interpreter.RSLInterpreterValueTypes;

public record ReturnValue(RuntimeValue value) implements RuntimeValue {
    @Override
    public RSLInterpreterValueTypes type() {
        return RSLInterpreterValueTypes.RETURN;
    }

    @Override
    public String toString() {
        return "Return(" + value.toString() + ")";
    }
}
