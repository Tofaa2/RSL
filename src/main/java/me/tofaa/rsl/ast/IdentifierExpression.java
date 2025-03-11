package me.tofaa.rsl.ast;

public record IdentifierExpression(String value) implements Expression  {
    @Override
    public AstNodeType type() {
        return AstNodeType.IDENTIFIER;
    }
}
