package me.tofaa.rsl.ast;

public record NullLiteralExpression(String value) implements Expression  {

    public static NullLiteralExpression INSTANCE = new NullLiteralExpression("null");

    @Override
    public boolean isNull() {
        return true;
    }

    @Override
    public AstNodeType type() {
        return AstNodeType.NULL_LITERAL;
    }
}
