package me.tofaa.rsl.interpreter;

import me.tofaa.rsl.ast.*;
import me.tofaa.rsl.environment.Environment;
import me.tofaa.rsl.exception.RSLInterpretException;
import me.tofaa.rsl.interpreter.runtime.NullValue;
import me.tofaa.rsl.interpreter.runtime.NumberValue;
import me.tofaa.rsl.interpreter.runtime.RuntimeValue;

public final class RSLInterpreter {

    public RuntimeValue eval(Statement astNode, Environment env) {

        switch (astNode.type()) {
            case NUM_LITERAL -> {
                return new NumberValue(((NumericLiteralExpression)astNode).value());
            }
            case IDENTIFIER -> {
                return evalIdentifier((IdentifierExpression) astNode, env);
            }
            case BINARY_EXPR -> {
                return evalBinaryExpr((BinaryExpression) astNode, env);
            }
            case PROGRAM -> {
                return evalProgram((ProgramStatement) astNode, env);
            }

            case NULL_LITERAL -> {
                return NullValue.INSTANCE;
            }

            default -> {
                throw new RSLInterpretException("Unrecognized statement supplied: " + astNode);
            }
        }
    }

    private RuntimeValue evalIdentifier(IdentifierExpression id, Environment env) {
        return env.lookup(id.value());
    }

    private RuntimeValue evalProgram(ProgramStatement programStatement, Environment env) {
        RuntimeValue lastEvaled = NullValue.INSTANCE;

        for (var element : programStatement.body()) {
            lastEvaled = eval(element, env);
        }

        return lastEvaled;
    }

    private RuntimeValue evalBinaryExpr(BinaryExpression binop, Environment env) {
        var left = eval(binop.left(), env);
        var right = eval(binop.right(), env);

        if (left.type() == RSLInterpreterValueTypes.NUMBER && right.type() == RSLInterpreterValueTypes.NUMBER) {
            return evalNumericExpr((NumberValue) left, (NumberValue) right, binop.operator());
        }
        return  NullValue.INSTANCE;
    }

    private RuntimeValue evalNumericExpr(NumberValue left, NumberValue right, String operator) {
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
