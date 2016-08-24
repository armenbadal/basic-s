package engine;

/**
 * Created by Armen Badalian on 22.08.2016.
 */
public class RuntimeError extends Throwable {
    public RuntimeError( String msg )
    {
        super(msg);
    }
}
