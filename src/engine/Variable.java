package engine;

/**/
public class Variable implements Expression {
    private String name = null;

    public Variable( String na )
    {
        name = na;
    }

    @Override
    public Constant evaluate(Environment env )
    {
        return env.get(name);
    }

    @Override
    public String toString()
    {
        return name;
    }
}
