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
        // լոկալ կատարման միջավայր
        Environment envloc = new Environment();

        // լոկալ միջավայրում պարամետրերին համապատասխանեցվում են
        // փոխանցված արգումենտների հաշվարկված արժեքները
        int i = 0;
        for( Variable pri : function.parameters ) {
            Value avo = arguments.get(i++).evaluate(env);
            envloc.add(pri, avo);
        }

        // ֆունկցիայի մարմինը կատարվում է լոկալ կատարման միջավայրում
        function.body.execute(envloc);
        // որպես արժեք վերադարձվում է լոկալ միջավայրում ֆունկցիայի անունով
        // փոփոխականի արժեքը
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
