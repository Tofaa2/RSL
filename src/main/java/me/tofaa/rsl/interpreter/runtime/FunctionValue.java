package me.tofaa.rsl.interpreter.runtime;

import me.tofaa.rsl.ast.Statement;
import me.tofaa.rsl.Environment;
import me.tofaa.rsl.interpreter.RSLInterpreterValueTypes;

import java.util.List;

public record FunctionValue(
        String name,
        List<String> params,
        Environment parentScope,
        List<Statement> body
) implements RuntimeValue{
    @Override
    public RSLInterpreterValueTypes type() {
        return RSLInterpreterValueTypes.FUNCTION;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("fn ").append(name).append("(");
        for (int i = 0; i< params.size(); i++) {
            var param = params.get(i);
            var last = i == params.size() - 1;
            sb.append(param);
            if (!last) {
                sb.append(",");
            }
        }
        sb.append(")");
        return sb.toString();
    }
}
