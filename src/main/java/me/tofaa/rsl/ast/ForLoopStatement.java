package me.tofaa.rsl.ast;

import java.util.List;

public record ForLoopStatement(
        String variable,
        Expression upperBound,
        boolean inclusive,
        List<Statement> body
) implements Expression{
    @Override
    public AstNodeType type() {
        return AstNodeType.LOOP_FOR;
    }
}
