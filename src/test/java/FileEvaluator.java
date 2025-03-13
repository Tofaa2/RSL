import me.tofaa.rsl.RSLScript;

import java.io.File;

public class FileEvaluator {

    public static void main(String[] args) {
        if (false) {
            RSLScript script = new RSLScript()
                    .registerDefaultGlobals();
            var res = script.eval(new File("samples/test.rsl").toPath());
            System.out.println("Evaluation result: " + res);
            return;
        }

        File[] files = new File("samples").listFiles(file -> file.getName().endsWith(".rsl"));
        if (files == null) {
            System.out.println("No files found to interpret.");
            return;
        }
        for (var file : files) {
            RSLScript script = new RSLScript()
                    .registerDefaultGlobals();
            var res = script.eval(file.toPath());
            System.out.println("Evaluation result: " + res);

//            RSLLexer lexer = RSLLexer.of(file);
//            RSLParser parser = new RSLParser(lexer);
//            ProgramStatement program = parser.create();
//            Environment env = new Environment(null);
//            GlobalNativeFunctions.register(env);
//            var result = RSLInterpreter.eval(program, env);
        }
    }

}
