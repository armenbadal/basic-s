package interpreter;

/**
 * Created by Armen Badalian on 21.08.2016.
 */
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
    public Constant evaluate(Environment env ) throws RuntimeError
    {
        Constant res0 = subexpro.evaluate(env);
        Constant res1 = subexpri.evaluate(env);

        double resval = 0.0;
        switch( operation ) {
            case "+":
                resval = res0.value + res1.value;
                break;
            case "-":
                resval = res0.value - res1.value;
                break;
            case "*":
                resval = res0.value * res1.value;
                break;
            case "/":
                if( res1.value == 0.0 )
                    throw new RuntimeError("Բաժանում զրոյի վրա։");
                resval = res0.value / res1.value;
                break;
            case "^":
                resval = Math.pow(res0.value, res1.value);
                break;
            case "=":
                resval = res0.value == res1.value ? 1.0 : 0.0;
                break;
            case "<>":
                resval = res0.value != res1.value ? 1.0 : 0.0;
                break;
            case ">":
                resval = res0.value > res1.value ? 1.0 : 0.0;
                break;
            case ">=":
                resval = res0.value >= res1.value ? 1.0 : 0.0;
                break;
            case "<":
                resval = res0.value < res1.value ? 1.0 : 0.0;
                break;
            case "<=":
                resval = res0.value <= res1.value ? 1.0 : 0.0;
                break;
        }

        return new Constant(resval);
    }

    @Override
    public String toString()
    {
        return String.format("(%s %s %s)", subexpro, operation, subexpri);
    }
}