package unittests;

import interpreter.Constant;
import interpreter.Environment;
import interpreter.Function;
import org.junit.Test;
import parser.Parser;

/**
 * Created by Armen Badalian on 21.08.2016.
 */
public class EvaluateTest {
    @Test
    public void tesst_a() throws Exception
    {
        String text =
                "FUNCTION max(x, y)\n" +
                "  IF x > y THEN\n" +
                "    max = x\n" +
                "  ELSE\n" +
                "    max = y\n" +
                "  END IF\n" +
                "END FUNCTION\n" +
                "\n" +
                "FUNCTION Main()\n" +
                "  c = max(7, 32)\n" +
                "  PRINT c\n" +
                "END FUNCTION\n";

        Parser parser0 = new Parser(text);
        parser0.parse();
        for( Function fi : parser0.subroutines )
            System.out.println(fi);
        Function Main0 = parser0.subroutines.get(1);

        Environment env0 = new Environment();
        Main0.body.execute(env0);
    }
}
