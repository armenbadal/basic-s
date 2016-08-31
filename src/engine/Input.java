package engine;

/**/
public class Input implements Statement {
    private Variable varname = null;

    public Input( Variable vn )
    {
        varname = vn;
    }

    @Override
    public void execute( Environment env )
    {
        java.util.Scanner scan = new java.util.Scanner(System.in);
        double value = scan.nextDouble();
        env.update(varname, new Value(value));
    }

    @Override
    public String toString()
    {
        return String.format("INPUT %s", varname);
    }
}
