package unittests;

import interpreter.Function;
import org.junit.Test;
import parser.Parser;

import static org.junit.Assert.*;

/**
 * Created by Armen Badalian on 21.08.2016.
 */
public class ParserTest {
    @Test
    public void test_a() throws Exception
    {
        Parser  parser0 = new Parser("DECLARE FUNCTION f(x, y)\n");
        parser0.parse();
        for(Function fi : parser0.subroutines )
            System.out.println(fi.toString());
    }

    @Test
    public void test_b() throws Exception
    {
        Parser  parser0 = new Parser("FUNCTION f(x, y)\n  c = x^2 + y^2\nEND FUNCTION\n");
        parser0.parse();
        for(Function fi : parser0.subroutines )
            System.out.println(fi.toString());
    }

    @Test
    public void test_c() throws Exception
    {
        String text = "FUNCTION f(x, y)\n" +
                "  c = x^2 + y^2\n" +
                "  IF c > 0 THEN\n" +
                "    PRINT x\n" +
                "    PRINT y\n" +
                "    PRINT c\n" +
                "  ELSE\n" +
                "    PRINT 777\n" +
                "  END IF\n" +
                "END FUNCTION\n";

        Parser  parser0 = new Parser(text);
        parser0.parse();
        for(Function fi : parser0.subroutines )
            System.out.println(fi.toString());
    }

    @Test
    public void test_d() throws Exception
    {
        String text = "FUNCTION f(x, y)\n" +
                "  FOR i = 1 + x TO y^2 STEP 1.5\n" +
                "    PRINT i\n" +
                "  END FOR\n" +
                "END FUNCTION\n";

        Parser  parser0 = new Parser(text);
        parser0.parse();
        for(Function fi : parser0.subroutines )
            System.out.println(fi.toString());
    }
}