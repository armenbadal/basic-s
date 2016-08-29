package engine;

import java.util.List;

/**/
public class BuiltIn implements Expression {
    private static final String[] predefined = { "SQR", "ABS" };

    public static boolean isInternal( String nm )
    {
        for( String ir : predefined )
            if( nm.equals(ir) )
                return true;
        return false;
    }

    private String name = null;
    private List<Expression> arguments = null;

    public BuiltIn(String nm, List<Expression> ags )
    {
        name = nm;
        arguments = ags;
    }

    @Override
    public Value evaluate(Environment env ) throws RuntimeError
    {
        if( name.equals("SQR") ) {
            Value a0 = arguments.get(0).evaluate(env);
            return new Value(Math.sqrt(a0.real));
        }

        if( name.equals("ABS") ) {
            Value a0 = arguments.get(0).evaluate(env);
            return new Value(Math.abs(a0.real));
        }

        return null;
    }

    @Override
    public String toString()
    {
        return String.format("%s(%s)", name, arguments.get(0));
    }
}
