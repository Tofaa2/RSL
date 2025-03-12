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

    @Override
    public boolean equals(Object o) {
        if (o instanceof ObjectValue(Map<String, RuntimeValue> properties1)) {
            if (properties.size() != properties1.size()) return false;
            for (var a : properties.entrySet()) {
                if (!properties1.containsKey(a.getKey())) {
                    return false;
                }
                if (!properties1.get(a.getKey()).equals(a.getValue())) {
                    return false;
                }
            }
        }
        return false;
    }
}
