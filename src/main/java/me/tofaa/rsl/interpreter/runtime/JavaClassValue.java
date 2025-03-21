package me.tofaa.rsl.interpreter.runtime;

import me.tofaa.rsl.ReflectionUtils;
import me.tofaa.rsl.interpreter.RSLInterpreterValueTypes;

import java.util.List;

public record JavaClassValue(Class<?> clazz) implements JavaProxiedRuntimeValue {

    public RuntimeValue callMethod(String name, List<RuntimeValue> args) {
        var list = JavaProxiedRuntimeValue.wrapInterpretedArray(args).toArray(new Object[0]);
        return JavaProxiedRuntimeValue.wrapPrimary(ReflectionUtils.callStaticMethod(clazz, name, list));
    }

    public void setField(RuntimeValue name, RuntimeValue value) {
        String s = name.asString().value();
        ReflectionUtils.setStaticField(clazz, s, JavaProxiedRuntimeValue.wrapInterpreted(value));
    }

    public RuntimeValue getField(RuntimeValue name) {
        String s = name.asString().value();
        return JavaProxiedRuntimeValue.wrapPrimary(ReflectionUtils.getStaticField(clazz, s));
    }

    @Override
    public RSLInterpreterValueTypes type() {
        return RSLInterpreterValueTypes.JAVA_CLASS;
    }

    @Override
    public String toString() {
        return "JavaClass<" + clazz.getSimpleName() + ">";
    }
}
