import me.tofaa.rsl.lexer.Lexer;
import me.tofaa.rsl.parser.RSLParser;

import java.io.File;
import java.util.Scanner;

public class AstTest {


    public static void main(String[] args) {
        System.out.println("RSL Repl v1.0");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("> ");
            String line = scanner.nextLine();
            if (line == null || line.isBlank() || line.equalsIgnoreCase("exit")) {
                break;
            }
            RSLParser parser = new RSLParser(Lexer.of(line));
            var program = parser.create();
            System.out.println(program.toString());
        }
    }

}
