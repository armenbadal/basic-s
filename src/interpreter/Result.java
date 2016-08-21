package interpreter;

/**/
public class Result implements Statement {
    private Expression expro = null;

    public Result( Expression exo )
    {
        expro = exo;
    }

    @Override
    public void execute( Environment env )
    {
        Constant valo = expro.evaluate(env);
        env.add("$$", valo);
    }
}
