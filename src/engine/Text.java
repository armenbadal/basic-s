package engine;

/**/
public class Text implements Expression {
    public String value = null;

    public Text( String vl )
    {
        value = vl;
    }

    @Override
    public Value evaluate( Environment env )
    {
        return new Value(value);
    }

    @Override
    public String toString()
    {
        return String.format("\"%s\"", value);
    }
}
