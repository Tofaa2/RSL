package me.tofaa.rsl.ast;

import java.util.List;

public record WhileLoopStatement(
        Expression condition,
        List<Statement> body
) implements Expression{
    @Override
    public AstNodeType type() {
        return AstNodeType.LOOP_WHILE;
    }
}
