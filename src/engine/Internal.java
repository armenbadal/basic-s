package engine;

import java.util.List;

/**/
public class Internal implements Expression {
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

    public Internal( String nm, List<Expression> ags )
    {
        name = nm;
        arguments = ags;
    }

    @Override
    public Constant evaluate( Environment env ) throws RuntimeError
    {
        if( name.equals("SQR") ) {
            Constant a0 = arguments.get(0).evaluate(env);
            return new Constant(Math.sqrt(a0.value));
        }

        if( name.equals("ABS") ) {
            Constant a0 = arguments.get(0).evaluate(env);
            return new Constant(Math.abs(a0.value));
        }

        return null;
    }

    @Override
    public String toString()
    {
        return String.format("%s(%s)", name, arguments.get(0));
    }
}
