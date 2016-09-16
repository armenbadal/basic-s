package engine;

/**/
public class Unary implements Expression {
    private String operation = null;
    private Expression subexpr = null;

    public Unary( String op, Expression se )
    {
        operation = op;
        subexpr = se;
    }

    @Override
    public Value evaluate( Environment env ) throws RuntimeError
    {
        Value res = subexpr.evaluate(env);
        return Value.calculate(operation, res);
    }

    @Override
    public String toString()
    {
        return String.format("(%s %s)", operation, subexpr);
    }
}
