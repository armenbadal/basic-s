package engine;

/**/
public class Dim implements Statement {
    public Variable name = null;
    public int size = 0;

    public Dim( Variable nm, int sz )
    {
        name = nm;
        size = sz;
    }

    @Override
    public void execute( Environment env )
    {
        // TODO ստուգել նույն անունով փոփոխականի կամ ֆունկցիայի հայտարարված լինելը
        
        Array arr = new Array(size);
        Value val = new Value(arr);

        env.add(name, val);
    }

    @Override
    public String toString()
    {
        return String.format("DIM %s(%d)", name, size);
    }
}
