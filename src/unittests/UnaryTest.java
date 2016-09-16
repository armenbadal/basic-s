package unittests;

import engine.*;
import org.junit.Test;

import static org.junit.Assert.*;

/**/
public class UnaryTest {
    @Test
    public void evaluate() throws Exception, RuntimeError
    {
        Environment env = new Environment();
        Unary u0 = new Unary("-", new Real(3.14));
        Value r0 = u0.evaluate(env);
        assertEquals(r0, new Value(-3.14));
    }
}