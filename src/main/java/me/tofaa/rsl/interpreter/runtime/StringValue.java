package me.tofaa.rsl.interpreter.runtime;

import me.tofaa.rsl.interpreter.RSLInterpreterValueTypes;

public record StringValue(String value) implements RuntimeValue {
    @Override
    public RSLInterpreterValueTypes type() {
        return RSLInterpreterValueTypes.STRING;
    }
}
