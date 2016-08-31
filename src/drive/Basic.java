package drive;

import engine.Environment;
import engine.Function;
import engine.RuntimeError;
import parser.Parser;
import parser.SyntaxError;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**/
public class Basic {
    private boolean execute( String filename )
    {
        StringBuilder texter = new StringBuilder();
        try( BufferedReader read = new BufferedReader(new FileReader(filename)) ) {
            read.lines().forEach(e -> texter.append(e).append("\n"));
        }
        catch( Exception ex ) {
            System.err.println(ex.getMessage());
        }

        // վերլուծել
        Parser parser = new Parser(texter.toString());
        List<Function> parsed;
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
        Function entry = parsed.stream()
                .filter(e -> e.name.equals("Main"))
                .findFirst().get();
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

    private static void runTests()
    {
        try {
            Path dir = Paths.get("./cases");
            for( Path nm : Files.newDirectoryStream(dir, "*.bas") ) {
                System.out.printf("~ ~ ~ ~ ~ ~ ~ %s ~ ~ ~ ~ ~ ~ ~\n", nm);
                Basic basic = new Basic();
                //if( nm.endsWith("test06.bas") )
                    basic.execute(nm.toString());
            }
        }
        catch( IOException ex ) {
            System.err.println(ex.getMessage());
        }
    }

    public static void main( String[] args )
    {
        if( args[0].equals("test") )
            runTests();
        else {
            Basic basic = new Basic();
            boolean result = basic.execute(args[0]);
            System.exit(result ? 0 : 1);
        }
    }
}
