import me.tofaa.rsl.GlobalNativeFunctions;
import me.tofaa.rsl.ast.ProgramStatement;
import me.tofaa.rsl.environment.Environment;
import me.tofaa.rsl.interpreter.RSLInterpreter;
import me.tofaa.rsl.lexer.Lexer;
import me.tofaa.rsl.lexer.RSLParser;

import java.io.File;
import java.sql.SQLOutput;

public class FileEvaluator {

    public static void main(String[] args) {
        File[] files = new File("samples").listFiles(file -> file.getName().endsWith(".rsl"));
        if (files == null) {
            System.out.println("No files found to interpret.");
            return;
        }
        for (var file : files) {
            Lexer lexer = Lexer.of(file);
            RSLParser parser = new RSLParser(lexer);
            ProgramStatement program = parser.create();
            Environment env = new Environment(null);
            GlobalNativeFunctions.register(env);
            var result = RSLInterpreter.eval(program, env);
        }
    }

}
