package me.tofaa.rsl.interpreter.runtime;

import me.tofaa.rsl.ast.Statement;
import me.tofaa.rsl.environment.Environment;
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


}
