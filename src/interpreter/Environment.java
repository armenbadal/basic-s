package interpreter;

import java.util.*;

/**/
public class Environment {
    private class Pair {
        public String name = null;
        public Constant value = null;

        public Pair( String nm, Constant vl )
        {
            name = nm;
            value = vl;
        }
    }

    private Stack<List<Pair>> scopes = null;

    public Environment()
    {
        scopes = new Stack<>();
        newScope();
    }

    public void newScope()
    {
        scopes.add(new ArrayList<>());
    }

    public void popScope()
    {
        scopes.pop();
    }

    public void add( String vr, Constant vl )
    {
        scopes.peek().add(new Pair(vr,vl));
    }

    public void update( String vr, Constant vl )
    {
        Pair pki = lookup(vr);
        if( pki == null )
            add(vr, vl);
        else
            pki.value = vl;
    }

    private Pair lookup( String vr )
    {
        for( List<Pair> sci : scopes )
            for( Pair pk : sci )
                if( pk.name.equals(vr) )
                    return pk;
        return null;
    }

    public Constant get(String var )
    {
        Pair pri = lookup(var);
        return pri != null ? pri.value : null;
    }
}
