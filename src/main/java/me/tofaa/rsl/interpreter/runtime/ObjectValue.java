package me.tofaa.rsl.interpreter.runtime;

import me.tofaa.rsl.interpreter.RSLInterpreterValueTypes;

import java.util.Map;

public record ObjectValue(
        Map<String, RuntimeValue> properties
) implements RuntimeValue {

    @Override
    public RSLInterpreterValueTypes type() {
        return RSLInterpreterValueTypes.OBJECT;
    }

}
