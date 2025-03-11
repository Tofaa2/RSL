package me.tofaa.rsl.ast;

import java.util.List;

public record ProgramStatement(List<Statement> body) implements Statement {
    @Override
    public String toString() {
        return "ProgramStatement{\n" +
                "   body=" + body +
                "\n}";
    }

    @Override
    public AstNodeType type() {
        return AstNodeType.PROGRAM;
    }
}
