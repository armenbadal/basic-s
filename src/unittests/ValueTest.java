package unittests;

import engine.RuntimeError;
import engine.Value;
import org.junit.Test;

import static org.junit.Assert.*;

/**/
public class ValueTest {
    @Test
    public void calculateUnary() throws Exception, RuntimeError
    {
        // բացասում
        Value v0 = new Value(3.14);
        Value r0 = Value.calculate("-", v0);
        assertTrue(r0.real < 0);

        Value v1 = new Value(-3.14);
        Value r1 = Value.calculate("-", v1);
        assertTrue(r1.real > 0);

        // ժխտում
        Value v2 = new Value(1);
        Value r2 = Value.calculate("NOT", v2);
        assertEquals(r2.real, 0, 0);

        Value v3 = new Value(0);
        Value r3 = Value.calculate("NOT", v3);
        assertEquals(r3.real, 1, 0);

        // ունար գործողություն տողի նկատմամբ
        try {
            Value v4 = new Value("Horatio");
            Value r4 = Value.calculate("NOT", v4);
        }
        catch( RuntimeError re ) {
            String mes = re.getMessage();
            assertEquals(mes, "Տողի համար NOT գործողություն սահմանված չէ։");
        }
    }

    @Test
    public void calculateBinary() throws Exception, RuntimeError
    {
        Value v0 = new Value("Laertes");
        Value v1 = new Value("Polonius");
        Value r0 = Value.calculate("&", v0, v1);
        assertEquals(r0, new Value("LaertesPolonius"));
    }

    @Test
    public void equalsReal() throws Exception
    {
        Value v0 = new Value(3.14);
        Value v1 = new Value(3.14);
        Value v2 = new Value(2);

        assertEquals(v0, v1);
        assertNotEquals(v2,2);
    }

    @Test
    public void equalsText() throws Exception
    {
        Value v3 = new Value("Hamlet");
        Value v4 = new Value("Hamlet");
        Value v5 = new Value("Ophelia");

        assertEquals(v3, v4);
        assertNotEquals(v3,v5);
    }
}