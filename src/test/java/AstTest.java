import me.tofaa.rsl.parser.RSLParser;

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
            var program = new RSLParser(line).create();
            System.out.println(program.toString());
        }
    }

}
