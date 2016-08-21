package interpreter;

import java.util.ArrayList;
import java.util.List;

/**/
public class Sequence implements Statement {
    private List<Statement> statements = null;

    public Sequence()
    {
        statements = new ArrayList<>();
    }

    public void add( Statement ste )
    {
        statements.add(ste);
    }

    @Override
    public void execute( Environment env )
    {
        for( Statement sti : statements )
            sti.execute(env);
    }

    @Override
    public String toString()
    {
        ArrayList<String> stals = new ArrayList<>();
        statements.forEach(e -> stals.add(e.toString()));
        return String.join("\n", stals);
    }
}
