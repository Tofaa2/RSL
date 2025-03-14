package me.tofaa.rsl.interpreter.runtime;

import me.tofaa.rsl.ReflectionUtils;
import me.tofaa.rsl.interpreter.RSLInterpreterValueTypes;

public record JavaEnumValue(Class<?> clazz) implements JavaProxiedRuntimeValue {

    public RuntimeValue getMember(RuntimeValue name) {
        return wrapPrimary(ReflectionUtils.getEnumMember(clazz, name.asString().value()));
    }

    @Override
    public RSLInterpreterValueTypes type() {
        return RSLInterpreterValueTypes.JAVA_ENUM;
    }
}
