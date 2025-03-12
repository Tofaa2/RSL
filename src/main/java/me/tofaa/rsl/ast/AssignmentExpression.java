package me.tofaa.rsl.ast;

public record AssignmentExpression(
        Expression assigned,
        Expression value
) implements Expression{
    @Override
    public AstNodeType type() {
        return AstNodeType.ASSIGNMENT_EXPR;
    }
}
