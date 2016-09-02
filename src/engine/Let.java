package engine;

/**/
public class Let implements Statement {
    private Variable vari = null;
    private Expression valu = null;

    public Let(Variable vn, Expression vl )
    {
        vari = vn;
        valu = vl;
    }

    @Override
    public void execute( Environment env ) throws RuntimeError
    {
        Value val = valu.evaluate(env);

        if( vari.isElement() ) {
            Array arv = env.get(vari).array;
            Value ix = vari.index.evaluate(env);
            arv.set((int)ix.real, val);
        }
        else
            env.update(vari, val);
    }

    @Override
    public String toString()
    {
        return String.format("%s = %s", vari, valu);
    }
}
