package engine;

/**/
public class Binary implements Expression {
    private String operation = null;
    private Expression subexpro = null;
    private Expression subexpri = null;

    public Binary( String op, Expression seo, Expression sei )
    {
        operation = op;
        subexpro = seo;
        subexpri = sei;
    }

    @Override
    public Value evaluate( Environment env ) throws RuntimeError
    {
        Value res0 = subexpro.evaluate(env);
        Value res1 = subexpri.evaluate(env);

        // տեքստային գործողություն
        if( res0.type == Value.TEXT && res1.type == Value.TEXT ) {
            if( operation.equals("&") )
                return new Value(res0.text + res1.text);
            else
                throw new RuntimeError("Տեքստերի համար %s գործողությունը որոշված չէ։",
                        operation);
        }

        // թվային գործողություններ
        double resval = 0.0;
        switch( operation ) {
            case "+":
                resval = res0.real + res1.real;
                break;
            case "-":
                resval = res0.real - res1.real;
                break;
            case "*":
                resval = res0.real * res1.real;
                break;
            case "/":
                if( res1.real == 0.0 )
                    throw new RuntimeError("Բաժանում զրոյի վրա։");
                resval = res0.real / res1.real;
                break;
            case "^":
                resval = Math.pow(res0.real, res1.real);
                break;
            case "=":
                resval = res0.real == res1.real ? 1.0 : 0.0;
                break;
            case "<>":
                resval = res0.real != res1.real ? 1.0 : 0.0;
                break;
            case ">":
                resval = res0.real > res1.real ? 1.0 : 0.0;
                break;
            case ">=":
                resval = res0.real >= res1.real ? 1.0 : 0.0;
                break;
            case "<":
                resval = res0.real < res1.real ? 1.0 : 0.0;
                break;
            case "<=":
                resval = res0.real <= res1.real ? 1.0 : 0.0;
                break;
            case "AND":
                resval = (res0.real != 0) && (res1.real != 0) ? 1.0 : 0.0;
                break;
            case "OR":
                resval = (res0.real != 0) || (res1.real != 0) ? 1.0 : 0.0;
                break;
        }

        return new Value(resval);
    }

    @Override
    public String toString()
    {
        return String.format("(%s %s %s)", subexpro, operation, subexpri);
    }
}