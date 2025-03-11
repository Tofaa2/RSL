package me.tofaa.rsl.interpreter.runtime;

import me.tofaa.rsl.interpreter.RSLInterpreterValueTypes;

public record NullValue(String value) implements RuntimeValue {

    public static NullValue INSTANCE = new NullValue("null");

    @Override
    public RSLInterpreterValueTypes type() {
        return RSLInterpreterValueTypes.NULL;
    }
}
