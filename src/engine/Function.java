package engine;

import java.util.List;
import java.util.stream.Stream;

/**/
public class Function {
    public String name = null;
    public List<Variable> parameters = null;
    public Statement body = null;

    public Function( String nm, List<Variable> pr )
    {
        name = nm;
        parameters = pr;
    }

    @Override
    public boolean equals( Object oj )
    {
        if( !(oj instanceof Function) )
            return false;

        Function of = (Function)oj;
        if( !of.name.equals(name) )
            return false;
        if( of.parameters.size() != parameters.size() )
            return false;
        // TODO ստուգել նաև պարամետրերի անունները

        return true;
    }

    @Override
    public String toString()
    {
        final String pastr = parameters.stream()
                .map(Variable::toString)
                .reduce((x,y) -> x + ", " + y)
                .get();

        if( body == null )
            return String.format("DECLARE FUNCTION %s(%s)", name, pastr);

        return String.format("FUNCTION %s(%s)\n%sEND FUNCTION",
                name, pastr, body == null ? "" : body);
    }
}
