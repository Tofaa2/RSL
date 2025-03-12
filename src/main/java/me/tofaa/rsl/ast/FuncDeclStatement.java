package me.tofaa.rsl.ast;

import java.util.List;

public record FuncDeclStatement(
        List<String> params,
        String name,
        List<Statement> body
) implements Statement{
    @Override
    public AstNodeType type() {
        return AstNodeType.FUNC_DECL;
    }
}
