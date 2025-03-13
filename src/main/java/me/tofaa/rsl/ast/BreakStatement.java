package me.tofaa.rsl.ast;

import me.tofaa.rsl.interpreter.runtime.BreakValue;

public record BreakStatement() implements Expression{

    public static final BreakStatement INSTANCE = new BreakStatement();

    @Override
    public AstNodeType type() {
        return AstNodeType.BREAK;
    }
}
