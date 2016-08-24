package engine;

/**/
public interface Statement {
    void execute( Environment env ) throws RuntimeError;
}
