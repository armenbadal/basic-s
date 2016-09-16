package engine;

/**/
public class Input implements Statement {
    private Variable vari = null;

    public Input( Variable vn )
    {
        vari = vn;
    }

    @Override
    public void execute( Environment env )
    {
        System.out.print("? ");
        java.util.Scanner scan = new java.util.Scanner(System.in);
        if( vari.type == Variable.TEXT ) {
            String value = scan.nextLine();
            env.update(vari, new Value(value));
        }
        else {
            double value = scan.nextDouble();
            env.update(vari, new Value(value));
        }
    }

    @Override
    public String toString()
    {
        return String.format("INPUT %s", vari);
    }
}
