package engine;

/**/
public class Value {
    public static final char REAL = 'R';
    public static final char TEXT = 'T';
    public static final char ARRAY = 'A';

    public char kind = REAL;

    public double real = 0.0;
    public String text = null;
    public Array array = null;

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

    public Value( Array vl )
    {
        kind = ARRAY;
        array = vl;
    }

    public Value calculate( String oper ) throws RuntimeError
    {
        if( kind == TEXT )
            throw new RuntimeError("Տողի համար %s գործողություն սահմանված չէ։", oper);

        if( oper.equals("-") )
            return new Value(-real);

        if( oper.equals("NOT") )
            return new Value(real == 0 ? 1 : 0);

        throw new RuntimeError("Չսահմանված ունար գործողություն․ %s", oper);
    }

    public Value calculate( String oper, Value vlo ) throws RuntimeError
    {
        if( kind == TEXT && vlo.kind == TEXT ) {
            if( oper.equals("&") )
                return new Value(text + vlo.text);

            throw new RuntimeError("Տեքստերի համար %s գործողությունը սահմանված չէ։", oper);
        }

        if( kind != REAL || vlo.kind != REAL )
            throw new RuntimeError("%s գործողության օպերանդները համաձայնեցված չեն։", oper);

        // TODO իրականացնել բինար գործողությունները

        return null;
    }

    @Override
    public boolean equals( Object other )
    {
        if( !(other instanceof Value) )
            return false;

        Value oval = (Value)other;

        if( kind == REAL && oval.kind == REAL )
            return real == oval.real;

        if( kind == TEXT && oval.kind == TEXT )
            return text.equals(oval.text);

        // TODO ստուգել զանգվածների հավասարությունը
        return false;
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
