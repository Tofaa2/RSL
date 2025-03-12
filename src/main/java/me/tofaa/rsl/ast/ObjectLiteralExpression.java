package me.tofaa.rsl.ast;

import java.util.List;

public record ObjectLiteralExpression(
        List<PropertyLiteralExpression> properties
) implements Expression{
    @Override
    public AstNodeType type() {
        return AstNodeType.OBJECT;
    }
}
