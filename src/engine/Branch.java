package engine;

/**/
public class Branch implements Statement {
    private Expression condition = null;
    private Statement decision = null;
    private Statement alternative = null;

    public Branch( Expression co, Statement de )
    {
        condition = co;
        decision = de;
    }

    public void setElse( Statement al )
    {
        alternative = al;
    }


    @Override
    public void execute( Environment env ) throws RuntimeError
    {
        Constant conval = condition.evaluate(env);
        if( conval.value != 0.0 )
            decision.execute(env);
        else {
            if( alternative != null )
                alternative.execute(env);
        }
    }

    @Override
    public String toString()
    {
        StringBuilder srbu = new StringBuilder();
        srbu.append("IF ");
        srbu.append(condition);
        srbu.append(" THEN\n");
        srbu.append(decision);
        if( alternative != null ) {
            srbu.append("ELSE\n");
            srbu.append(alternative);
        }
        srbu.append("END IF");

        return srbu.toString();
    }
}
