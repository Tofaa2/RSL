package me.tofaa.rsl;

import me.tofaa.rsl.interpreter.RSLInterpreter;
import me.tofaa.rsl.interpreter.runtime.NullValue;
import me.tofaa.rsl.interpreter.runtime.ReturnValue;
import me.tofaa.rsl.interpreter.runtime.RuntimeValue;
import me.tofaa.rsl.parser.RSLParser;

import java.nio.file.Path;

@SuppressWarnings("UnusedReturnValue")
public class RSLScript {

    private final Environment globalScope;

    public RSLScript() {
        this.globalScope = new Environment(null);
    }

    public RSLScript registerDefaultGlobals() {
        GlobalNativeFunctions.register(globalScope);
        return this;
    }

    public RuntimeValue eval(String source) {
        RSLParser parser = new RSLParser(source);
        return RSLInterpreter.eval(parser.create(), globalScope);
    }

    public RuntimeValue eval(Path path) {
        RSLParser parser = new RSLParser(Utils.readFileContent(path));
        var evaled = RSLInterpreter.eval(parser.create(), globalScope);
        if (evaled instanceof ReturnValue(RuntimeValue value)) { // Unbox return value
             return value;
        }
        return evaled;
    }

    public Environment globalScope() {
        return globalScope;
    }
}
