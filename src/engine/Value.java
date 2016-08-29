package engine;

/**/
public class Value implements Expression {
    public static final char REAL = 'R';
    public static final char TEXT = 'T';

    public char kind = REAL;
    public double real = 0.0;
    public String text = null;

    public Value( double vl )
    {
        kind = REAL;
        real = vl;
    }

    public Value( String vl )
    {
        kind = TEXT;
        text = vl;
    }

    @Override
    public Value evaluate( Environment env ) throws RuntimeError
    {
        return this;
    }

    @Override
    public String toString()
    {
        if( kind == REAL )
            return String.valueOf(real);

        if( kind == TEXT )
            return text;

        return null;
    }
}
