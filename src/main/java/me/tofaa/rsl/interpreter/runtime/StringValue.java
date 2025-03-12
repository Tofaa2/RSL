package me.tofaa.rsl.interpreter.runtime;

import me.tofaa.rsl.interpreter.RSLInterpreterValueTypes;

public record StringValue(String value) implements RuntimeValue {
    @Override
    public RSLInterpreterValueTypes type() {
        return RSLInterpreterValueTypes.STRING;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof String s) { // Might be a bad idea at some point but dont care.
            return s.equals(value);
        }
        if (o instanceof StringValue(String v)) {
            return v.equals(value);
        }
        return false;
    }
}
