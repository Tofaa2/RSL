package me.tofaa.rsl;

import me.tofaa.rsl.exception.RSLInterpretException;
import me.tofaa.rsl.interpreter.runtime.RuntimeValue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class Environment {

    private Environment parent;
    private Map<String, RuntimeValue> variables;
    private Set<String> constants;

    public Environment(Environment parent) {
        this.parent = parent;
        this.variables = new HashMap<>();
        this.constants = new HashSet<>();
    }

    public RuntimeValue lookup(String name) {
        var env = resolve(name);
        return env.variables.get(name);
    }

    public RuntimeValue declare(String name, RuntimeValue value, boolean constant) {
        if (variables.containsKey(name)) {
            throw new RSLInterpretException("Attempted to redefine an existing variable %s".formatted(name));
        }
        else {
            variables.put(name, value);
        }
        if (constant) {
            constants.add(name);
        }
        return value;
    }

    public RuntimeValue assign(String name, RuntimeValue value) {
        var env = resolve(name);
        if (env.constants.contains(name)) {
            throw new RSLInterpretException("Attempted to reassign constant variable %s. Consider changing it to var.".formatted(name));
        }
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
