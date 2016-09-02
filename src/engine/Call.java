package engine;

import java.util.ArrayList;
import java.util.List;

/**/
public class Call implements Statement {
    private ApplyFunc appsub = null;

    public Call(Function fu, List<Expression> ag )
    {
        appsub = new ApplyFunc(fu, ag);
    }

    @Override
    public void execute( Environment env ) throws RuntimeError
    {
        appsub.evaluate(env);
    }

    @Override
    public String toString()
    {
        ArrayList<String> argis = new ArrayList<>();
        appsub.arguments.forEach(e -> argis.add(e.toString()));
        return String.format("CALL %s %s", appsub.function.name, String.join(", ", argis));
    }
}
