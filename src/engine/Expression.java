package engine;

/**/
public interface Expression {
    Constant evaluate(Environment env ) throws RuntimeError;
}
