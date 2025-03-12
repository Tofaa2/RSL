package me.tofaa.rsl.ast;

public record StringLiteralExpression(String value) implements Expression{
    @Override
    public AstNodeType type() {
        return AstNodeType.STRING_LITERAL;
    }
}
