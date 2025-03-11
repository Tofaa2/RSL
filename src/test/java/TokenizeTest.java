import me.tofaa.rsl.lexer.Lexer;
import me.tofaa.rsl.lexer.Token;

import java.io.File;

public class TokenizeTest {

    public static void main(String[] args) {
        var lexer = Lexer.of(new File("samples/test.rsl"));
        for (Token t : lexer.tokenize()) {
            System.out.printf("Token of type: %s with value %s%n", t.type().name(), t.value());
        }
    }

}
