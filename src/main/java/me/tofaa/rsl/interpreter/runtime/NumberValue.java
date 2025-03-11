package me.tofaa.rsl.interpreter.runtime;

import me.tofaa.rsl.interpreter.RSLInterpreterValueTypes;

public record NumberValue(Number value) implements RuntimeValue{
    @Override
    public RSLInterpreterValueTypes type() {
        return RSLInterpreterValueTypes.NUMBER;
    }
}
