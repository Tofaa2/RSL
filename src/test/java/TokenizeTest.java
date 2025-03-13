import me.tofaa.rsl.Utils;
import me.tofaa.rsl.parser.RSLLexer;
import me.tofaa.rsl.parser.Token;

import java.io.File;

public class TokenizeTest {

    public static void main(String[] args) {
        var lexer = RSLLexer.tokenize(Utils.readFileContent(new File("samples/test.rsl").toPath()));
        for (Token t : lexer) {
            System.out.printf("Token of type: %s with value %s%n", t.type().name(), t.value());
        }
    }

}
