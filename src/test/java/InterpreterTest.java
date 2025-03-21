import me.tofaa.rsl.Environment;
import me.tofaa.rsl.interpreter.RSLInterpreter;
import me.tofaa.rsl.parser.RSLParser;

import java.util.Scanner;

public class InterpreterTest {

    public static void main(String[] args) {
        System.out.println("RSL Repl v1.0");
        Scanner scanner = new Scanner(System.in);

        Environment env = new Environment(null);

        while (true) {
            System.out.print("> ");
            String line = scanner.nextLine();
            if (line == null || line.isBlank() || line.equalsIgnoreCase("exit")) {
                System.out.println("Exiting repl...");
                break;
            }
            RSLParser parser = new RSLParser(line);
            var program = parser.create();

            long n = System.nanoTime();
            var result = RSLInterpreter.eval(program, env);
            long end = System.nanoTime();
            System.out.println(result);
            System.out.println("Execution finished in " + (end - n) + " ns");
        }
    }

}
