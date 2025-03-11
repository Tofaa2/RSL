package me.tofaa.rsl.interpreter;

import me.tofaa.rsl.ast.NumericLiteralExpression;
import me.tofaa.rsl.ast.Statement;
import me.tofaa.rsl.interpreter.runtime.NullValue;
import me.tofaa.rsl.interpreter.runtime.NumberValue;
import me.tofaa.rsl.interpreter.runtime.RuntimeValue;

public final class RSLInterpreter {

    public RuntimeValue eval(Statement astNode) {

        switch (astNode.type()) {
            case NUM_LITERAL -> {
                return new NumberValue(((NumericLiteralExpression)astNode).value());
            }

            default -> {
                return NullValue.INSTANCE;
            }
        }

    }

}
