package engine;

/**/
public class Array {
    private int size = 0;
    private Value[] elements = null;

    public Array( int sz )
    {
        size= sz;
        elements = new Value[size];
    }

    public void set( int ix, Value vl )
    {
        elements[ix] = vl;
    }

    public Value get( int ix )
    {
        return elements[ix];
    }
}
