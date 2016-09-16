package unittests;

import com.sun.xml.internal.ws.developer.UsesJAXBContext;
import engine.Environment;
import engine.RuntimeError;
import engine.Value;
import engine.Variable;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**/
public class VariableTest {
    private Environment enviro = null;

    @Before
    public void setUp() throws Exception
    {
        enviro = new Environment();
        enviro.add(new Variable("x"), new Value(3.14));
        enviro.add(new Variable("y$"), new Value("Macbeth"));
    }

    @Test
    public void evaluate() throws Exception, RuntimeError
    {
        Variable v0 = new Variable("x");
        Value r0 = v0.evaluate(enviro);
        assertEquals(r0.real, 3.14, 0.001);

        Variable v1 = new Variable("y$");
        Value r1 = v1.evaluate(enviro);
        assertEquals(r1.text, "Macbeth");
    }

    @Test
    public void variableKind() throws Exception
    {
        Variable v0 = new Variable("x");
        assertTrue(v0.type == Variable.REAL);

        Variable v1 = new Variable("y$");
        assertTrue(v1.type == Variable.TEXT);
    }
}