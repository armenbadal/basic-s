package engine;

/**
 * Created by Armen Badalian on 21.08.2016.
 */
public class Print implements Statement {
    private Expression priex = null;

    public Print( Expression exp )
    {
        priex = exp;
    }

    @Override
    public void execute( Environment env ) throws RuntimeError
    {
        Constant resv = priex.evaluate(env);
        System.out.println(resv.toString());
    }

    @Override
    public String toString()
    {
        return String.format("PRINT %s", priex);
    }
}
