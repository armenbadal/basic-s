package engine;

/**/
public class For implements Statement {
    private Variable param = null;
    private Expression initial = null;
    private Expression limit = null;
    private Expression step = null;
    private Statement body = null;

    public For(Variable pr, Expression in, Expression li, Expression sp, Statement be )
    {
        param = pr;
        initial = in;
        limit = li;
        step = sp;
        body = be;
    }

    @Override
    public void execute( Environment env ) throws RuntimeError
    {
        Value vi = initial.evaluate(env);
        Value vb = limit.evaluate(env);
        Value sp = new Value(1);
        if( step != null )
            sp = step.evaluate(env);

        double prv = vi.real;
        env.update(param, vi);
        while( prv <=  vb.real ) {
            body.execute(env);
            prv += sp.real;
            env.update(param, new Value(prv));
        }
    }

    @Override
    public String toString()
    {
        StringBuilder srbu = new StringBuilder();
        srbu.append(String.format("FOR %s = %s TO %s", param, initial, limit));
        if( step != null )
            srbu.append(String.format(" STEP %s", step));
        srbu.append("\n");
        srbu.append(body);
        srbu.append("END FOR");

        return srbu.toString();
    }
}
