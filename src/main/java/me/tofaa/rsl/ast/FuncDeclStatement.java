package me.tofaa.rsl.ast;

import java.util.List;

public record FuncDeclStatement(
        List<String> params,
        String name,
        boolean unknown,
        List<Statement> body
) implements Expression {
    @Override
    public AstNodeType type() {
        return AstNodeType.FUNC_DECL;
    }
}
