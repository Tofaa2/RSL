package me.tofaa.rsl.interpreter;

import me.tofaa.rsl.ast.*;
import me.tofaa.rsl.Environment;
import me.tofaa.rsl.exception.RSLInterpretException;
import me.tofaa.rsl.interpreter.runtime.*;

import static me.tofaa.rsl.interpreter.EvalExpressions.executeBlock;
import static me.tofaa.rsl.interpreter.RSLInterpreter.eval;

final class EvalStatements {

    private EvalStatements() {}

    static RuntimeValue evalWhileStatement(WhileLoopStatement stmt, Environment env) {
        while (true) {
            var conditionResult = eval(stmt.condition(), env);
            if (!(conditionResult instanceof BooleanValue boolVal)) {
                throw new RSLInterpretException("While loop condition must evaluate to a boolean. Got: " + conditionResult.type());
            }

            if (!boolVal.b()) {
                break; // Exit loop when condition is false
            }

            var result = executeBlock(stmt.body(), env);
            if (result instanceof ReturnValue) {
                return result; // Propagate return value out of the loop
            }
            if (result instanceof BreakValue) {
                break;
            }
        }

        return NullValue.INSTANCE;
    }

    static RuntimeValue evalForLoop(ForLoopStatement stmt, Environment env) {
        var upperBoundVal = eval(stmt.upperBound(), env);

        if (!(upperBoundVal instanceof NumberValue upperBound)) {
            throw new RSLInterpretException("For loop upper bound must be a number. Got: " + upperBoundVal.type());
        }

        int upper = upperBound.value().intValue();
        NumberValue start = (NumberValue) env.lookup(stmt.variable());
        int limit = stmt.inclusive() ? upper : upper - 1; // Adjust exclusive range
        System.out.println("For Loop Debug -> Start: " + start + ", Upper: " + upper + ", Inclusive: " + stmt.inclusive());
        for (int i = start.value().intValue(); i <= limit; i++) {
            System.out.println("Loop Iter");
            env.assign(stmt.variable(), new NumberValue(i));

            for (var stmtBody : stmt.body()) {
                var result = eval(stmtBody, env);
                if (result instanceof BreakValue) {
                    return NullValue.INSTANCE; // Break out of loop
                } else if (result instanceof ReturnValue) {
                    return result; // Return early if needed
                }
            }
        }
        return NullValue.INSTANCE;
    }

    static RuntimeValue evalFuncDecl(FuncDeclStatement astNode, Environment env) {
        var fn = new FunctionValue(
                astNode.name(),
                astNode.params(),
                env,
                astNode.body()
        );
        return env.declare(astNode.name(), fn, true);
    }

    static RuntimeValue evalVarDecl(VarDeclStatement astNode, Environment env) {
        return env.declare(astNode.identifier(), eval(astNode.value(), env), astNode.constant());
    }

    static RuntimeValue evalProgram(ProgramStatement programStatement, Environment env) {
        RuntimeValue lastEvaled = NullValue.INSTANCE;

        for (var element : programStatement.body()) {
            var evaled = eval(element, env);
            if (element instanceof ReturnStatement) {
                lastEvaled = evaled;
            }
        }

        return lastEvaled;
    }


}
