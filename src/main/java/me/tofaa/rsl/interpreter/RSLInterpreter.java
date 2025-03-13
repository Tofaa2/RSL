package me.tofaa.rsl.interpreter;

import me.tofaa.rsl.ast.*;
import me.tofaa.rsl.Environment;
import me.tofaa.rsl.exception.RSLInterpretException;
import me.tofaa.rsl.interpreter.runtime.*;

import static me.tofaa.rsl.interpreter.EvalStatements.*;
import static me.tofaa.rsl.interpreter.EvalExpressions.*;

public final class RSLInterpreter {

    public static RuntimeValue eval(Statement astNode, Environment env) {

        switch (astNode.type()) {
            case OBJECT -> {
                return evalObject((ObjectLiteralExpression) astNode, env);
            }
            case IF -> {
                return evalIfStatement((IfStatement) astNode, env);
            }
            case BREAK -> {
                return BreakValue.INSTANCE;
            }
            case LOOP_FOR -> {
                return evalForLoop((ForLoopStatement) astNode, env);
            }
            case LOOP_WHILE -> {
                return evalWhileStatement((WhileLoopStatement) astNode, env);
            }
            case RETURN -> {
                return evalReturnStatement(((ReturnStatement)astNode), env);
            }
            case CALL_EXPR -> {
                return evalCallExpr((CallExpression) astNode, env);
            }
            case ASSIGNMENT_EXPR -> {
                return evalAssignment((AssignmentExpression) astNode, env);
            }
            case NUM_LITERAL -> {
                return new NumberValue(((NumericLiteralExpression)astNode).value());
            }
            case BOOL_LITERAL -> {
                return evalBooleanExpression((BooleanLiteral)astNode, env);
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
            case VAR_DECL -> {
                return evalVarDecl((VarDeclStatement)astNode, env);
            }
            case FUNC_DECL -> {
                return evalFuncDecl((FuncDeclStatement)astNode, env);
            }
            case NULL_LITERAL -> {
                return NullValue.INSTANCE;
            }
            case STRING_LITERAL -> {
                return new StringValue(((StringLiteralExpression)astNode).value());
            }
            case MEMBER_EXPR -> {
                return evalObjectField((MemberExpression)astNode, env);
            }

            default -> {
                throw new RSLInterpretException("Unrecognized statement supplied: " + astNode);
            }
        }
    }

}
