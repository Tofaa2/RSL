package me.tofaa.rsl.ast;

public record PropertyLiteralExpression(String key, Expression value) implements Expression {

    @Override
    public AstNodeType type() {
        return AstNodeType.PROPERTY;
    }

}
