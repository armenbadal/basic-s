package engine;

/**/
public class RuntimeError extends Throwable {
    public RuntimeError( String msg, Object... els )
    {
        super(String.format(msg, els));
    }
}
