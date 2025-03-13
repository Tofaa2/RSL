package me.tofaa.rsl.ast;

import java.util.List;

public record IfStatement(
        Expression condition,
        List<Statement> body,
        List<IfStatement> elifBlocks,
        List<Statement> elseBlock
) implements Expression {
    @Override
    public AstNodeType type() {
        return AstNodeType.IF;
    }
}
