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

    public Value calculate( String oper ) throws RuntimeError
    {
        if( kind == TEXT ) {
            String message = String.format("Տողի համար %s գործողություն սահմանված չէ։", oper);
            throw new RuntimeError(message);
        }

        // TODO իրականացնել ունար գործողությունները

        return null;
    }

    public Value calculate( String oper, Value vlo ) throws RuntimeError
    {
        if( kind == TEXT && vlo.kind == TEXT ) {
            if( oper.equals("+") )
                return new Value(text + vlo.text);

            String message = String.format("Տեքստերի համար %s գործողությունը սահմանված չէ։", oper);
            throw new RuntimeError(message);
        }

        if( kind != REAL || vlo.kind != REAL ) {
            String message = String.format("%s գործողության օպերանդները համաձայնեցված չեն։", oper);
            throw new RuntimeError(message);
        }

        // TODO իրականացնել բինար գործողությունները

        return null;
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
