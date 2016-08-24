package engine;

import java.util.List;

/**/
public class Function {
    public String name = null;
    public List<String> parameters = null;
    public Statement body = null;

    public Function( String nm, List<String> pr, Statement bo )
    {
        name = nm;
        parameters = pr;
        body = bo;
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
