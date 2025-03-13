package me.tofaa.rsl.interpreter.runtime;

import me.tofaa.rsl.interpreter.RSLInterpreterValueTypes;

public record BooleanValue(

        Boolean b
) implements RuntimeValue {

    public static final BooleanValue TRUE = new BooleanValue(true);
    public static final BooleanValue FALSE = new BooleanValue(false);

    @Override
    public RSLInterpreterValueTypes type() {
        return RSLInterpreterValueTypes.BOOL;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof BooleanValue(Boolean b1)) {
            return b == b1;
        }
        return false;
    }
}
