package me.tofaa.rsl.ast;

public record NumericLiteralExpression(Number value) implements Expression{
    @Override
    public AstNodeType type() {
        return AstNodeType.NUM_LITERAL;
    }
}
