package parser;

/**/
public class ParseError extends Exception {
    public ParseError( String msg, Object... els )
    {
        super(String.format(msg, els));
    }
}
