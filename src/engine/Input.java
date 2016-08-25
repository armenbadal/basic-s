package engine;

/**
 * Created by Armen Badalian on 21.08.2016.
 */
public class Input implements Statement {
    private String varname = null;

    public Input( String vn )
    {
        varname = vn;
    }

    @Override
    public void execute( Environment env )
    {
        java.util.Scanner scan = new java.util.Scanner(System.in);
        double value = scan.nextDouble();
        env.update(varname, new Constant(value));
    }

    @Override
    public String toString()
    {
        return String.format("INPUT %s", varname);
    }
}