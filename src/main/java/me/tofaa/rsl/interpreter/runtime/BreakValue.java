package me.tofaa.rsl.interpreter.runtime;


import me.tofaa.rsl.interpreter.RSLInterpreterValueTypes;

public record BreakValue() implements RuntimeValue{

    public static BreakValue INSTANCE = new BreakValue();

    @Override
    public String toString() {
        return "ScopeBreak";
    }

    @Override
    public RSLInterpreterValueTypes type() {
        return RSLInterpreterValueTypes.BREAK;
    }
}
