package me.tofaa.rsl.interpreter.runtime;

import me.tofaa.rsl.interpreter.RSLInterpreterValueTypes;

public record NumberValue(Number value) implements RuntimeValue{
    @Override
    public RSLInterpreterValueTypes type() {
        return RSLInterpreterValueTypes.NUMBER;
    }

    public int asInt() {
        return value.intValue();
    }

    public long asLong() {
        return value.longValue();
    }

    public short asShort() {
        return value.shortValue();
    }

    public byte asByte() {
        return value.byteValue();
    }

    public float asFloat() {
        return value.floatValue();
    }

    public double asDouble()
    {
        return value.doubleValue();
    }
    @Override
    public String toString() {
        return String.valueOf(value);
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
