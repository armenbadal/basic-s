package engine;

/**/
public interface Expression {
    Value evaluate(Environment env ) throws RuntimeError;
}
