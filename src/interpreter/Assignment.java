package interpreter;

/**/
public class Assignment implements Statement {
    private String varnm = null;
    private Expression valu = null;

    public Assignment(String vn, Expression vl )
    {
        varnm = vn;
        valu = vl;
    }

    @Override
    public void execute( Environment env ) throws RuntimeError
    {
        Constant val = valu.evaluate(env);
        env.update(varnm, val);
    }

    @Override
    public String toString()
    {
        return String.format("%s = %s", varnm, valu);
    }
}
