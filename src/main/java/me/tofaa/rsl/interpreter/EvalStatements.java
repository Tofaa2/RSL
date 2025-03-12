package me.tofaa.rsl.interpreter;

import me.tofaa.rsl.ast.ProgramStatement;
import me.tofaa.rsl.ast.VarDeclStatement;
import me.tofaa.rsl.environment.Environment;
import me.tofaa.rsl.interpreter.runtime.NullValue;
import me.tofaa.rsl.interpreter.runtime.RuntimeValue;

import static me.tofaa.rsl.interpreter.RSLInterpreter.eval;

public final class EvalStatements {

    private EvalStatements() {}


    static RuntimeValue evalVarDecl(VarDeclStatement astNode, Environment env) {
        return env.declare(astNode.identifier(), eval(astNode.value(), env), astNode.constant());
    }

    static RuntimeValue evalProgram(ProgramStatement programStatement, Environment env) {
        RuntimeValue lastEvaled = NullValue.INSTANCE;

        for (var element : programStatement.body()) {
            lastEvaled = eval(element, env);
        }

        return lastEvaled;
    }


}
