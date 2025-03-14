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
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Object { ").append(System.lineSeparator());
        for (var values : properties.entrySet()) {
            sb.append(values.getKey()).append("=").append(values.getValue().toString());
            sb.append(System.lineSeparator());
        }
        sb.append(" }");
        return sb.toString();
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
