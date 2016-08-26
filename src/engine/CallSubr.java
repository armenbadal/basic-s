package engine;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abadalian on 8/23/16.
 */
public class CallSubr implements Statement {
    private ApplyFunc appsub = null;

    public CallSubr(Function fu, List<Expression> ag )
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
