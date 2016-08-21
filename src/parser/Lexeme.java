package parser;

/**/
public class Lexeme {
    public Token kind = Token.Unknown;
    public String value = null;
    public int line = 0;

    public Lexeme( Token kn, int ps )
    {
        this(kn, null, ps);
    }

    public Lexeme( Token kn, String vl, int ps )
    {
        kind = kn;
        value = vl;
        line = ps;
    }

    public boolean is( Token... exps )
    {
        for( Token ex : exps )
            if( kind == ex )
                return true;
        return false;
    }

    @Override
    public String toString()
    {
        return String.format("<%s|%s|%d>", kind, value, line);
    }
}
