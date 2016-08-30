package engine;

/**/
public class Variable implements Expression {
    public static final char REAL = 'R';
    public static final char TEXT = 'T';

    public String name = null;
    public char type = REAL;

    public Variable( String na )
    {
        type = na.endsWith("$") ? TEXT : REAL;
        name = na;
    }

    @Override
    public Value evaluate(Environment env ) throws RuntimeError
    {
        Value val = env.get(name);
        if( val == null )
            throw new RuntimeError(String.format("Չսահմանված փոփոխական %s", name));
        return val;
    }

    @Override
    public String toString()
    {
        return name;
    }
}
