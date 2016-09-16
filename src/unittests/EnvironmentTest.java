package unittests;

import engine.Environment;
import engine.Value;
import engine.Variable;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**/
public class EnvironmentTest {
    private Environment enviro = null;

    @Before
    public void setUp() throws Exception
    {
        enviro = new Environment();
    }

    @Test
    public void add() throws Exception
    {
        enviro.add(new Variable("x"), new Value(3.14));
        Value val = enviro.get(new Variable("x"));
        assertTrue(0 == Double.compare(val.real, 3.14));
    }

    @Test
    public void update() throws Exception
    {
        enviro.add(new Variable("x"), new Value(3.14));
        enviro.update(new Variable("x"), new Value(6.28));
        Value val = enviro.get(new Variable("x"));
        assertTrue(0 == Double.compare(val.real, 6.28));
    }
}