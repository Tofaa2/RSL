package me.tofaa.rsl.interpreter.runtime;

import me.tofaa.rsl.ReflectionUtils;
import me.tofaa.rsl.interpreter.RSLInterpreterValueTypes;

import java.util.List;

public record JavaEnumValue(Class<?> clazz) implements JavaProxiedRuntimeValue {

    public RuntimeValue getMember(RuntimeValue name) {
        return JavaProxiedRuntimeValue.wrapPrimary(ReflectionUtils.getEnumMember(clazz, name.asString().value()));
    }

    public RuntimeValue callMethod(String name, List<RuntimeValue> args) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public RSLInterpreterValueTypes type() {
        return RSLInterpreterValueTypes.JAVA_ENUM;
    }
}
