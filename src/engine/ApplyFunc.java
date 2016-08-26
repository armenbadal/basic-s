package engine;

import java.util.ArrayList;
import java.util.List;

/**/
public class ApplyFunc implements Expression {
    public Function function = null;
    public List<Expression> arguments = null;

    public ApplyFunc( Function fu, List<Expression> ag )
    {
        function = fu;
        arguments = ag;
    }

    @Override
    public Constant evaluate( Environment env ) throws RuntimeError
    {
        Environment envloc = new Environment();

        List<Constant> argvs = new ArrayList<>();
        for (Expression eo : arguments)
            argvs.add(eo.evaluate(env));

        int i = 0;
        for( String pri : function.parameters )
            envloc.add(pri, argvs.get(i++));

        function.body.execute(envloc);
        Constant rval = envloc.get(function.name);

        return rval;
    }

    @Override
    public String toString()
    {
        ArrayList<String> argis = new ArrayList<>();
        arguments.forEach(e -> argis.add(e.toString()));
        return String.format("%s(%s)", function.name, String.join(", ", argis));
    }
}
