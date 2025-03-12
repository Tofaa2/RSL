package me.tofaa.rsl.interpreter.runtime;

import me.tofaa.rsl.interpreter.RSLInterpreterValueTypes;

public record NumberValue(Number value) implements RuntimeValue{
    @Override
    public RSLInterpreterValueTypes type() {
        return RSLInterpreterValueTypes.NUMBER;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof NumberValue(Number a)) {
            return this.value.doubleValue() == a.doubleValue();
        }
         else {
             return false;
        }
    }
}
