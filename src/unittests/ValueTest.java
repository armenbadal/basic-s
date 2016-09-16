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
        Value r0 = v0.calculate("-");
        assertTrue(r0.real < 0);

        Value v1 = new Value(-3.14);
        Value r1 = v1.calculate("-");
        assertTrue(r1.real > 0);

        // ժխտում
        Value v2 = new Value(1);
        Value r2 = v1.calculate("NOT");
        assertEquals(r2.real, 0, 0);

        Value v3 = new Value(0);
        Value r3 = v1.calculate("NOT");
        assertEquals(r3.real, 0, 0);

        // ունար գործողություն տողի նկատմամբ
        try {
            Value v4 = new Value("Horatio");
            Value r4 = v4.calculate("NOT");
        }
        catch( RuntimeError re ) {
            String mes = re.getMessage();
            assertEquals(mes, "Տողի համար NOT գործողություն սահմանված չէ։");
        }
    }

    @Test
    public void equalsReal() throws Exception
    {
        Value v0 = new Value(3.14);
        Value v1 = new Value(3.14);
        Value v2 = new Value(2);

        assertEquals(v0, v1);
        assertNotEquals(v0,2);
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