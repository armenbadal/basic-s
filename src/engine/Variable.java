package engine;

/**/
public class Variable implements Expression {
    private String name = null;

    public Variable( String na )
    {
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
