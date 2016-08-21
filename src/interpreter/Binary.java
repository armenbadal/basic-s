package interpreter;

/**
 * Created by Armen Badalian on 21.08.2016.
 */
public class Binary implements Expression {
    private String operation = null;
    private Expression subexpro = null;
    private Expression subexpri = null;

    public Binary( String op, Expression seo, Expression sei )
    {
        operation = op;
        subexpro = seo;
        subexpri = sei;
    }

    @Override
    public Constant evaluate(Environment env )
    {
        return null;
    }

    @Override
    public String toString()
    {
        return String.format("(%s %s %s)", subexpro, operation, subexpri);
    }
}