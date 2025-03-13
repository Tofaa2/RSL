package me.tofaa.rsl.interpreter.runtime;

import me.tofaa.rsl.ast.Expression;
import me.tofaa.rsl.interpreter.RSLInterpreterValueTypes;

public record ConditionalBooleanValue(
        Expression left,
        Expression right,
        String operator
) implements RuntimeValue{


    @Override
    public RSLInterpreterValueTypes type() {
        return RSLInterpreterValueTypes.BOOL;
    }
}
