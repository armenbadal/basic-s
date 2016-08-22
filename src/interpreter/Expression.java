package interpreter;

/**/
public interface Expression {
    Constant evaluate(Environment env ) throws RuntimeError;
}
