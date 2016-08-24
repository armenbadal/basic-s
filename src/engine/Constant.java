package engine;

/**/
public class Constant implements Expression {
    public double value = 0.0;

    public Constant(double vl )
    {
        value = vl;
    }

    @Override
    public Constant evaluate(Environment env )
    {
        return this;
    }

    @Override
    public String toString()
    {
        return String.valueOf(value);
    }
}
