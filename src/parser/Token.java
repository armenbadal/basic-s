package parser;

/**/
public class Token {
    public Kind kind = Kind.Unknown;
    public String lexeme = null;
    public int line = 0;

    public Token(Kind kn, int ps )
    {
        this(kn, null, ps);
    }

    public Token(Kind kn, String vl, int ps )
    {
        kind = kn;
        lexeme = vl;
        line = ps;
    }

    public boolean is( Kind... exps )
    {
        for( Kind ex : exps )
            if( kind == ex )
                return true;
        return false;
    }

    @Override
    public String toString()
    {
        return String.format("<%s|%s|%d>", kind, lexeme, line);
    }
}
