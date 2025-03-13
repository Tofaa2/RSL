package me.tofaa.rsl.interpreter;

import me.tofaa.rsl.ast.*;
import me.tofaa.rsl.Environment;
import me.tofaa.rsl.exception.RSLInterpretException;
import me.tofaa.rsl.interpreter.runtime.*;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static me.tofaa.rsl.interpreter.RSLInterpreter.eval;

final class EvalExpressions {

    private EvalExpressions() {}


    static RuntimeValue evalIfStatement(IfStatement stmt, Environment env) {
        var condition = eval(stmt.condition(), env);

        if (!(condition instanceof BooleanValue boolVal)) {
            throw new RSLInterpretException("If condition must evaluate to a boolean. Got: " + condition.type());
        }

        if (boolVal.b()) {
            var result = executeBlock(stmt.body(), env);
            if (result != NullValue.INSTANCE) {
                return result; // Return early if a return value was found
            }
        }

        // Check elif blocks
        for (var elifStmt : stmt.elifBlocks()) {
            var elifCondition = eval(elifStmt.condition(), env);
            if (!(elifCondition instanceof BooleanValue elifBool)) {
                throw new RSLInterpretException("Elif condition must evaluate to a boolean. Got: " + elifCondition.type());
            }

            if (elifBool.b()) {
                var result = executeBlock(elifStmt.body(), env);
                if (result != NullValue.INSTANCE) {
                    return result;
                }
            }
        }

        // Check else block
        if (stmt.elseBlock() != null) {
            var result = executeBlock(stmt.elseBlock(), env);
            if (result != NullValue.INSTANCE) {
                return result;
            }
        }

        return NullValue.INSTANCE;
    }

    // Helper method to execute a block and stop if a return statement is found
    static RuntimeValue executeBlock(List<Statement> body, Environment env) {
        for (var statement : body) {
            var result = eval(statement, env);
            if (result instanceof ReturnValue || result instanceof BreakValue) {
                return result; // Immediately propagate the return.
            }
        }
        return NullValue.INSTANCE;
    }

    static RuntimeValue evalReturnStatement(ReturnStatement stmt, Environment env) {
        RuntimeValue result = eval(stmt.expr(), env);
        return new ReturnValue(result);
    }

    static RuntimeValue evalCallExpr(CallExpression obj, Environment env) {
        var args = new ArrayList<RuntimeValue>();
        for (var a : obj.args()) {
            args.add(eval(a, env));
        }
        var fn = eval(obj.caller(), env);
        if (fn.type() == RSLInterpreterValueTypes.NATIVE_FUNCTION) {
            var func = (NativeFunctionValue)fn;
            var callable = func.call();
            return callable.call(env, args);
        }
        else if (fn.type() == RSLInterpreterValueTypes.FUNCTION) {
            var function = ((FunctionValue) fn);
            var scope = new Environment(function.parentScope());

            // Bind function parameters.
            for (int i = 0; i < function.params().size(); i++) {
                var name = function.params().get(i);
                if (i >= args.size()) {
                    throw new RSLInterpretException(
                            "Passed invalid amount of arguments into a function. Expected %s received %s".formatted(
                                    function.params().size(), args.size()
                            )
                    );
                }
                var argument = args.get(i);
                scope.declare(name, argument, false);
            }

            // Execute the function body and return as soon as a ReturnValue is encountered.
            for (var stmt : function.body()) {
                var res = eval(stmt, scope);
                if (res instanceof ReturnValue) {
                    // Unwrap the ReturnValue before returning from the function.
                    return ((ReturnValue) res).value();
                }
            }
            return NullValue.INSTANCE;
//            var function = ((FunctionValue)fn);
//            var scope = new Environment(function.parentScope());
//            for (int i = 0; i < function.params().size(); i++) {
//                var name = function.params().get(i);
//                if (i >= args.size()) {
//                    throw new RSLInterpretException(
//                            "Passed invalid amount of arguments into a function. Expected %s received %s".formatted(
//                                    String.valueOf(function.params().size()),
//                                    String.valueOf(args.size())
//                            )
//                    );
//                }
//                var argument = args.get(i);
//                scope.declare(name, argument, false); // Mutable fun!
//            }
//            RuntimeValue result = NullValue.INSTANCE;
//            for (var stmt : function.body()) {
//                var res = eval(stmt, scope);
//                if (stmt instanceof ReturnStatement) {
//                    result = res;
//                }
//            }
//            return result;
        }
        else {
            throw new RSLInterpretException("Attempted to call a non function value. Called: %s".formatted(fn));
        }
    }

    static RuntimeValue evalObject(ObjectLiteralExpression obj, Environment env) {
        var object = new ObjectValue(new HashMap<>());
        for (var a : obj.properties()) {
            var key = a.key();
            var value = a.value();
            RuntimeValue runtimeVal = (value == null || value.isNull()) ? env.lookup(key) : eval(value, env);
            object.properties().put(a.key(), runtimeVal);
        }
        return object;
    }

    static RuntimeValue evalObjectField(MemberExpression expr, Environment env) {
        RuntimeValue objectRuntimeVal = eval(expr.object(), env);
        if (!(objectRuntimeVal instanceof ObjectValue(java.util.Map<String, RuntimeValue> properties))) {
            throw new RSLInterpretException("Attempted to access a field on a non-object: " + expr.object());
        }

        RuntimeValue propertyRuntimeVal;
        if (expr.computed()) {
            // If computed, evaluate the property expression dynamically
            propertyRuntimeVal = eval(expr.property(), env);

            if (!(propertyRuntimeVal instanceof StringValue)) {
                throw new RSLInterpretException("Computed property must evaluate to a string: " + expr.property());
            }
        } else {
            // If not computed, directly use the static property (should be a string literal)
            if (!(expr.property() instanceof IdentifierExpression)) {
                throw new RSLInterpretException("Non-computed property must be a string literal: " + expr.property());
            }
            propertyRuntimeVal = new StringValue(((IdentifierExpression) expr.property()).value());
        }

        String propertyName = ((StringValue) propertyRuntimeVal).value();
        RuntimeValue propertyValue = properties.get(propertyName);

        if (propertyValue == null) {
            throw new RSLInterpretException("Property '" + propertyName + "' not found in object.");
        }

        return propertyValue;
    }

    static RuntimeValue evalAssignment(AssignmentExpression expr, Environment env) {
        if (expr.assigned().type() == AstNodeType.IDENTIFIER) {
            var name = ((IdentifierExpression)expr.assigned()).value();
            return env.assign(name, eval(expr.value(), env));
        }
        if (expr.assigned() instanceof MemberExpression memberExpr) {
            // Object property assignment (e.g., obj.complex.bar = 42)
            return evalObjectAssignment(memberExpr, expr.value(), env);
        }
        throw new RSLInterpretException(String.format("Attempted to reassign a non identifier. %s", expr.assigned().type()));
    }

    static RuntimeValue evalObjectAssignment(MemberExpression expr, Expression valueExpr, Environment env) {
        RuntimeValue objectRuntimeVal = eval(expr.object(), env);

        if (!(objectRuntimeVal instanceof ObjectValue object)) {
            throw new RSLInterpretException("Attempted to assign to a field of a non-object: " + expr.object());
        }

        RuntimeValue propertyRuntimeVal;
        if (expr.computed()) {
            propertyRuntimeVal = eval(expr.property(), env);

            if (!(propertyRuntimeVal instanceof StringValue stringValue)) {
                throw new RSLInterpretException("Computed property must evaluate to a string: " + expr.property());
            }
        } else {
            if (!(expr.property() instanceof IdentifierExpression identifierExpr)) {
                throw new RSLInterpretException("Non-computed property must be an identifier: " + expr.property());
            }
            propertyRuntimeVal = new StringValue(identifierExpr.value());
        }

        String propertyName = ((StringValue) propertyRuntimeVal).value();

        // Evaluate the new value to assign
        RuntimeValue newValue = eval(valueExpr, env);

        // Assign the new value to the object
        object.properties().put(propertyName, newValue);

        return newValue;
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
        var operator = binop.operator();
        if (operator.equals("is")) {
            return new BooleanValue(left.equals(right));
        }
        if (operator.equals("not")) {
            return new BooleanValue(!left.equals(right));
        }
        if (left.type() == RSLInterpreterValueTypes.NUMBER && right.type() == RSLInterpreterValueTypes.NUMBER) {
            return evalNumericExpr((NumberValue) left, (NumberValue) right, binop.operator());
        }
        else if (left.type() == RSLInterpreterValueTypes.STRING
                && right.type() == RSLInterpreterValueTypes.NUMBER || right.type() == RSLInterpreterValueTypes.STRING
        ) {
            return evalStringExpr((StringValue)left, right, binop.operator());
        }
        return  NullValue.INSTANCE;
    }

    static RuntimeValue evalStringExpr(StringValue s1, RuntimeValue s2, String operator) {
        if (Objects.equals(operator, "+")) {
            if (s2 instanceof StringValue(String value)) {
                return new StringValue(s1.value() + value);
            }
            else {
                if (s2 instanceof NumberValue(Number value)) {
                    return new StringValue(s1.value() + value);
                }
                throw new RSLInterpretException("Invalid string operation. Strings only support multiply (str * 4) or addition (str + other str).");
            }
        }
        else if (Objects.equals(operator, "*")) {
            if (s2 instanceof NumberValue(Number value)) {
                return new StringValue(s1.value().repeat(value.intValue()));
            }
            else {
                throw new RSLInterpretException("Invalid string operation. Strings only support multiply (str * 4) or addition (str + other str).");
            }
        }
        else {
            throw new RSLInterpretException("Invalid string operation. Strings only support multiply (str * 4) or addition (str + other str).");
        }
    }

    static RuntimeValue evalNumericExpr(NumberValue left, NumberValue right, String operator) {
        switch (operator) {
            case "<" -> {
                return new BooleanValue(
                        left.value().doubleValue() < right.value().doubleValue()
                );
            }
            case "<=" -> {
                return new BooleanValue(
                        left.value().doubleValue() <= right.value().doubleValue()
                );
            }
            case ">" -> {
                return new BooleanValue(
                        left.value().doubleValue() > right.value().doubleValue()
                );
            }
            case ">=" -> {
                return new BooleanValue(
                        left.value().doubleValue() >= right.value().doubleValue()
                );
            }
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
        throw new RSLInterpretException(
                "Invalid numeric operation. Received %s %s %s".formatted(
                        left.toString(), operator, right.toString()
                ));
    }

}
