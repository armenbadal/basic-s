package engine;

/**/
public class Variable implements Expression {
    public static final char REAL = 'R';
    public static final char TEXT = 'T';

    public String name = null;
    public char type = REAL;

    public Variable( String na )
    {
        type = REAL;
        name = na;

        if( isText() ) {
            type = TEXT;
            name = name.substring(0, name.length() - 1);
        }
    }

    public boolean isText()
    {
        return name.endsWith("$");
    }

    @Override
    public Value evaluate( Environment env ) throws RuntimeError
    {
        Value val = env.get(this);
        if( val == null )
            throw new RuntimeError(String.format("Չսահմանված փոփոխական %s", name));
        return val;
    }

    @Override
    public boolean equals( Object oj )
    {
        if( !(oj instanceof Variable) )
            return false;

        Variable vle = (Variable)oj;
        return name.equals(vle.name) && type == vle.type;
    }

    @Override
    public String toString()
    {
        if( type == TEXT )
            return name + "$";
        return name;
    }
}
