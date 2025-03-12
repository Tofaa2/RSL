package me.tofaa.rsl.ast;

import me.tofaa.rsl.interpreter.runtime.RuntimeValue;

public record VarDeclStatement(
        boolean constant,
        String identifier,
        Expression value // NULLABLE
) implements Statement{

    @Override
    public AstNodeType type() {
        return AstNodeType.VAR_DECL;
    }
}
