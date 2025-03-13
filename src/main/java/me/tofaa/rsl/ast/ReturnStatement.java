package me.tofaa.rsl.ast;

public record ReturnStatement(Expression expr) implements Expression{
    @Override
    public AstNodeType type() {
        return AstNodeType.RETURN;
    }
}
