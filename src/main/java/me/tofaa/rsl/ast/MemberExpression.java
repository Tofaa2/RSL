package me.tofaa.rsl.ast;

public record MemberExpression(
        Expression object,
        Expression property,
        boolean computed
) implements Expression{
    @Override
    public AstNodeType type() {
        return AstNodeType.MEMBER_EXPR;
    }
}
