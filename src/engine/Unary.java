package engine;

/**
 * Created by Armen Badalian on 21.08.2016.
 */
public class Unary implements Expression {
    private String operation = null;
    private Expression subexpr = null;

    public Unary( String op, Expression se )
    {
        operation = op;
        subexpr = se;
    }

    @Override
    public Constant evaluate(Environment env ) throws RuntimeError
    {
        Constant res = subexpr.evaluate(env);
        if( operation.equals("-") )
            res.value = -res.value;
        else if( operation.equals("NOT") )
            res.value = res.value == 0 ? 0 : 1;
        return res;
    }

    @Override
    public String toString()
    {
        return String.format("%s%s", operation, subexpr);
    }
}
