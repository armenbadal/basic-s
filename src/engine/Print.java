package engine;

/**/
public class Print implements Statement {
    private Expression priex = null;

    public Print( Expression exp )
    {
        priex = exp;
    }

    @Override
    public void execute( Environment env ) throws RuntimeError
    {
        Value resv = priex.evaluate(env);
        System.out.println(resv);
    }

    @Override
    public String toString()
    {
        return String.format("PRINT %s", priex);
    }
}
