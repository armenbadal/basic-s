package interpreter;

/**
 * Created by Armen Badalian on 21.08.2016.
 */
public class WhileLoop implements Statement {
    private Expression condition = null;
    private Statement body = null;

    public WhileLoop( Expression cond, Statement bdy )
    {
        condition = cond;
        body = bdy;
    }

    @Override
    public void execute( Environment env ) throws RuntimeError
    {
        Constant cond = condition.evaluate(env);
        while( cond.value != 0.0 ) {
            body.execute(env);
            cond = condition.evaluate(env);
        }
    }

    @Override
    public String toString()
    {
        return String.format("WHILE %s\n%sEND WHILE");
    }
}
