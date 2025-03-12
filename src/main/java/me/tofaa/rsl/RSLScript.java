package me.tofaa.rsl;

import me.tofaa.rsl.environment.Environment;
import me.tofaa.rsl.interpreter.RSLInterpreter;
import me.tofaa.rsl.interpreter.runtime.RuntimeValue;
import me.tofaa.rsl.lexer.Lexer;
import me.tofaa.rsl.lexer.RSLParser;

import java.nio.file.Path;

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
        Lexer lexer = Lexer.of(source);
        RSLParser parser = new RSLParser(lexer);
        return RSLInterpreter.eval(parser.create(), globalScope);
    }

    public RuntimeValue eval(Path path) {
        Lexer lexer = Lexer.of(path);
        RSLParser parser = new RSLParser(lexer);
        return RSLInterpreter.eval(parser.create(), globalScope);
    }

    public Environment globalScope() {
        return globalScope;
    }
}
