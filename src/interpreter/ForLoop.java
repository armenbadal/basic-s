package interpreter;

/**
 * Created by Armen Badalian on 21.08.2016.
 */
public class ForLoop implements Statement {
    private String param = null;
    private Expression initial = null;
    private Expression limit = null;
    private Expression step = null;
    private Statement body = null;

    public ForLoop( String pr, Expression in, Expression li, Expression sp, Statement be )
    {
        param = pr;
        initial = in;
        limit = li;
        step = sp;
        body = be;
    }

    @Override
    public void execute( Environment env )
    {
        Constant vi = initial.evaluate(env);
        Constant vb = limit.evaluate(env);
        Constant sp = new Constant(1);
        if( step != null )
            sp = step.evaluate(env);

        double prv = vi.value;
        env.update(param, vi);
        while( prv <=  vb.value ) {
            body.execute(env);
            prv += sp.value;
            env.update(param, new Constant(prv));
        }
    }

    @Override
    public String toString()
    {
        StringBuilder srbu = new StringBuilder();
        srbu.append(String.format("FOR %s = %s TO %s", param, initial, limit));
        if( step != null )
            srbu.append(String.format("STEP %s", step));
        srbu.append("\n");
        srbu.append(body);
        srbu.append("END FOR");

        return srbu.toString();
    }
}
