package interpreter;

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
    public Constant evaluate(Environment env )
    {
        return null;
    }

    @Override
    public String toString()
    {
        return String.format("%s%s", operation, subexpr);
    }
}
