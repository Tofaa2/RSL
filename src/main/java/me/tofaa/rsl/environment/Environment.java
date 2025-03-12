package me.tofaa.rsl.environment;

import me.tofaa.rsl.exception.RSLInterpretException;
import me.tofaa.rsl.interpreter.runtime.RuntimeValue;

import java.util.HashMap;
import java.util.Map;

public final class Environment {

    private Environment parent;
    private Map<String, RuntimeValue> variables;

    public Environment(Environment parent, Map<String, RuntimeValue> variables) {
        this.parent = parent;
        this.variables = variables;
    }

    public Environment(Environment parent) {
        this(parent, new HashMap<>());
    }

    public RuntimeValue lookup(String name) {
        var env = resolve(name);
        return env.variables.get(name);
    }

    public RuntimeValue declare(String name, RuntimeValue value) {
        if (variables.containsKey(name)) {
            throw new RSLInterpretException("Attempted to redefine an existing variable %s".formatted(name));
        }
        else {
            variables.put(name, value);
        }
        return value;
    }

    public RuntimeValue assign(String name, RuntimeValue value) {
        var env = resolve(name);
        env.variables.put(name, value);
        return value;
    }

    public Environment resolve(String name) {
        if (variables.containsKey(name)) {
            return this;
        }
        if (parent == null) {
            throw new RSLInterpretException("Cannot resolve variable with name %s".formatted(name));
        }
        return parent.resolve(name);
    }

    public Environment parent() {
        return parent;
    }

    public Map<String, RuntimeValue> variables() {
        return variables;
    }
}
