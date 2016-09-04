package engine;

import java.util.ArrayList;
import java.util.List;

/**/
public class Apply implements Expression {
    public Function function = null;
    public List<Expression> arguments = null;

    public Apply(Function fu, List<Expression> ag )
    {
        function = fu;
        arguments = ag;
    }

    @Override
    public Value evaluate( Environment env ) throws RuntimeError
    {
        Environment envloc = new Environment();

        List<Value> argvs = new ArrayList<>();
        for( Expression eo : arguments )
            argvs.add(eo.evaluate(env));

        int i = 0;
        for( Variable pri : function.parameters )
            envloc.add(pri, argvs.get(i++));

        function.body.execute(envloc);
        return envloc.get(new Variable(function.name));
    }

    @Override
    public String toString()
    {
        ArrayList<String> argis = new ArrayList<>();
        arguments.forEach(e -> argis.add(e.toString()));
        return String.format("%s(%s)", function.name, String.join(", ", argis));
    }
}