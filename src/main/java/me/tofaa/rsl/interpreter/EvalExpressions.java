package me.tofaa.rsl.interpreter;

import me.tofaa.rsl.ast.*;
import me.tofaa.rsl.environment.Environment;
import me.tofaa.rsl.exception.RSLInterpretException;
import me.tofaa.rsl.interpreter.runtime.BooleanValue;
import me.tofaa.rsl.interpreter.runtime.NullValue;
import me.tofaa.rsl.interpreter.runtime.NumberValue;
import me.tofaa.rsl.interpreter.runtime.RuntimeValue;

import static me.tofaa.rsl.interpreter.RSLInterpreter.eval;

public final class EvalExpressions {

    private EvalExpressions() {}

    static RuntimeValue evalAssignment(AssignmentExpression expr, Environment env) {
        // TODO: Objects
        if (expr.assigned().type() != AstNodeType.IDENTIFIER) {
            throw new RSLInterpretException(String.format("Attempted to reassign a non identifier. %s", expr.assigned().type()));
        }
        var name = ((IdentifierExpression)expr.assigned()).value();
        return env.assign(name, eval(expr.value(), env));
    }

    static RuntimeValue evalIdentifier(IdentifierExpression id, Environment env) {
        return env.lookup(id.value());
    }

    static RuntimeValue evalBooleanExpression(BooleanLiteral bool, Environment env) {
        if (bool.b()) {
            return BooleanValue.TRUE;
        }
        return BooleanValue.FALSE;
    }

    static RuntimeValue evalBinaryExpr(BinaryExpression binop, Environment env) {
        var left = eval(binop.left(), env);
        var right = eval(binop.right(), env);

        if (left.type() == RSLInterpreterValueTypes.NUMBER && right.type() == RSLInterpreterValueTypes.NUMBER) {
            return evalNumericExpr((NumberValue) left, (NumberValue) right, binop.operator());
        }
        return  NullValue.INSTANCE;
    }

    static RuntimeValue evalNumericExpr(NumberValue left, NumberValue right, String operator) {
        switch (operator) {
            case "+" -> {
                if (left.value() instanceof Double || right.value() instanceof Double) {
                    return new NumberValue(left.value().doubleValue() + right.value().doubleValue());
                }
                else {
                    return new NumberValue(left.value().intValue() + right.value().intValue());
                }
            }
            case "-" -> {
                if (left.value() instanceof Double || right.value() instanceof Double) {
                    return new NumberValue(left.value().doubleValue() - right.value().doubleValue());
                }
                else {
                    return new NumberValue(left.value().intValue() - right.value().intValue());
                }
            }
            case "*" -> {
                if (left.value() instanceof Double || right.value() instanceof Double) {
                    return new NumberValue(left.value().doubleValue() * right.value().doubleValue());
                }
                else {
                    return new NumberValue(left.value().intValue() * right.value().intValue());
                }
            }
            case "/" -> {
                if (left.value() instanceof Double || right.value() instanceof Double) {
                    return new NumberValue(left.value().doubleValue() / right.value().doubleValue());
                }
                else {
                    return new NumberValue(left.value().intValue() / right.value().intValue());
                }
            }
            case "%" -> {
                if (left.value() instanceof Double || right.value() instanceof Double) {
                    return new NumberValue(left.value().doubleValue() % right.value().doubleValue());
                }
                else {
                    return new NumberValue(left.value().intValue() % right.value().intValue());
                }
            }
        }
        throw new RSLInterpretException("Invalid numeric operation.");
    }

}
