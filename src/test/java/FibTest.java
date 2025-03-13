import me.tofaa.rsl.RSLScript;
import me.tofaa.rsl.interpreter.runtime.NumberValue;

import java.io.File;

public class FibTest {

    public static void main(String[] args) {
        RSLScript script = new RSLScript()
                .registerDefaultGlobals();
        var res = script.eval(new File("samples/fibonacci.rsl").toPath());
        int javaValue = fibonacci(10);
        if (res instanceof NumberValue) {
            System.out.println("Java version: " + javaValue + " RSL version: " + ((NumberValue) res).value().intValue());
        }
    }

    public static int fibonacci(int n)  {
        if(n == 0)
            return 0;
        else if(n == 1)
            return 1;
        else
            return fibonacci(n - 1) + fibonacci(n - 2);
    }

}
