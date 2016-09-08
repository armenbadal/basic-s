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
        if( operation.equals("-") )
            res = new Value(-res.real);
        else if( operation.equals("NOT") )
            res = new Value(res.real != 0 ? 0 : 1);
        return res;
    }

    @Override
    public String toString()
    {
        return String.format("(%s %s)", operation, subexpr);
    }
}
