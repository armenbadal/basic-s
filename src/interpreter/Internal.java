package interpreter;

import java.util.List;

/**
 * Created by Armen Badalian on 21.08.2016.
 */
public class Internal implements Expression {
    private String name = null;
    private List<Expression> arguments = null;

    public static boolean isInternal( String nm )
    {
        if( nm == "SQR" )
            return true;

        return false;
    }

    @Override
    public Constant evaluate( Environment env )
    {
        if( name.equals("SQR") ) {
            Constant a0 = arguments.get(0).evaluate(env);
            return new Constant(Math.sqrt(a0.value));
        }

        return null;
    }
}
