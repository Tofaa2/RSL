package me.tofaa.rsl.ast;

import java.util.List;

public record CallExpression(
        List<Expression> args,
        Expression caller
) implements Expression{
    @Override
    public AstNodeType type() {
        return AstNodeType.CALL_EXPR;
    }
}
