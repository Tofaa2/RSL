package me.tofaa.rsl.ast;

public record BinaryExpression(Expression left, Expression right, String operator) implements Expression {
    @Override
    public AstNodeType type() {
        return AstNodeType.BINARY_EXPR;
    }
}
