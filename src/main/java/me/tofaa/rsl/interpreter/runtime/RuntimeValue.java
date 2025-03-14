package me.tofaa.rsl.interpreter.runtime;

import me.tofaa.rsl.exception.RSLInterpretException;
import me.tofaa.rsl.interpreter.RSLInterpreterValueTypes;

// Lowest level representation of an interpreted value. This can be anything in the script.
public interface RuntimeValue {

    RSLInterpreterValueTypes type();

    String toString();

    default boolean isNull() {
        return this instanceof NullValue;
    }

    default NumberValue asNumber() {
        verifyType(RSLInterpreterValueTypes.NUMBER);
        return (NumberValue) this;
    }

    default StringValue asString() {
        verifyType(RSLInterpreterValueTypes.STRING);
        return (StringValue) this;
    }

    private void verifyType(RSLInterpreterValueTypes type) {
        if (type() != type) {
            throw new RSLInterpretException("Assertion fail. Expected %s Received %s".formatted(
                    type.name(),
                    type().name()
            ));
        }
    }

}
