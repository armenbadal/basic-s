package engine;

import java.util.List;

/**/
public class Function {
    public String name = null;
    public List<String> parameters = null;
    public Statement body = null;

    public Function( String nm, List<String> pr)
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
        if( body == null )
            return String.format("DECLARE FUNCTION %s(%s)",name,
                    String.join(", ", parameters));

        return String.format("FUNCTION %s(%s)\n%sEND FUNCTION",
                name,
                String.join(", ", parameters),
                body == null ? "" : body);
    }
}
