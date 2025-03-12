package me.tofaa.rsl.ast;

public record BooleanLiteral(Boolean b) implements Expression {
    @Override
    public AstNodeType type() {
        return AstNodeType.BOOL_LITERAL;
    }
}
