package engine;

/**/
public class Real implements Expression {
    public double value = 0.0;

    public Real( double vl )
    {
        value = vl;
    }

    @Override
    public Value evaluate( Environment env )
    {
        return new Value(value);
    }

    @Override
    public String toString()
    {
        return String.valueOf(value);
    }
}
