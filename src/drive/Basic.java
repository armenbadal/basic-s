package drive;

import interpreter.Environment;
import interpreter.Function;
import interpreter.RuntimeError;
import parser.Parser;
import parser.SyntaxError;

import java.util.List;

/**
 * Created by Armen Badalian on 21.08.2016.
 */
public class Basic {
    private boolean execute( String filename )
    {
        String text = null;
        // read contents of a file

        // վերլուծել
        Parser parser = new Parser(text);
        List<Function> parsed = null;
        try {
            parsed = parser.parse();
        }
        catch( SyntaxError se ) {
            System.err.println(se.getMessage());
            return false;
        }

        if( parsed == null )
            return false;

        // որոնել Main անունով ֆունկցիան
        Function entry = null;
        for( Function sri : parsed )
            if( sri.name.equals("Main") ) {
                entry = sri;
                break;
            }
        if( entry == null )
            return false;

        // կատարել
        Environment envo = new Environment();
        try {
            entry.body.execute(envo);
        }
        catch( RuntimeError re ) {
            System.err.println(re.getMessage());
        }

        return true;
    }

    public static void main( String[] args )
    {
        Basic basic = new Basic();
        boolean result = basic.execute(args[1]);

        System.exit(result ? 0 : 1);
    }
}
