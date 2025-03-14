package me.tofaa.rsl.interpreter.runtime;

import me.tofaa.rsl.ReflectionUtils;
import me.tofaa.rsl.interpreter.RSLInterpreterValueTypes;

import java.util.List;

public record JavaObjectValue(Object value) implements JavaProxiedRuntimeValue {

    public RuntimeValue callMethod(String name, List<RuntimeValue> args) {
        var list = wrapInterpretedArray(args).toArray(new Object[0]);
        return wrapPrimary(ReflectionUtils.callMemberMethod(value.getClass(), name, value, list));
    }

    public void setField(RuntimeValue name, RuntimeValue value) {
        String s = name.asString().value();
        ReflectionUtils.setMemberField(value.getClass(), s, value, wrapInterpreted(value));
    }

    public RuntimeValue getField(RuntimeValue name) {
        String s = name.asString().value();
        return wrapPrimary(ReflectionUtils.getMemberField(value.getClass(), s, value));
    }


    @Override
    public RSLInterpreterValueTypes type() {
        return RSLInterpreterValueTypes.JAVA_OBJECT;
    }
}
