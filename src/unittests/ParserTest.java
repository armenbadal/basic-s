package unittests;

import interpreter.Function;
import org.junit.Before;
import org.junit.Test;
import parser.Parser;

import static org.junit.Assert.*;

/**
 * Created by Armen Badalian on 21.08.2016.
 */
public class ParserTest {
    private Parser parser0 = null;

    @Before
    public void setUp() throws Exception
    {
        parser0 = new Parser("DECLARE FUNCTION f(x, y)\n");
    }

    @Test
    public void parse() throws Exception
    {
        parser0.parse();
        for(Function fi : parser0.subroutines )
            System.out.println(fi.toString());
    }

}