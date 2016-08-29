package engine;

import java.util.*;

/**/
public class Environment {
    private class Pair {
        public String name = null;
        public Value value = null;

        public Pair( String nm, Value vl )
        {
            name = nm;
            value = vl;
        }
    }

    private List<Pair> scope = null;

    public Environment()
    {
        scope = new ArrayList<>();
    }

    public void add( String vr, Value vl )
    {
        scope.add(new Pair(vr,vl));
    }

    public void update( String vr, Value vl )
    {
        Pair pki = lookup(vr);
        if( pki == null )
            add(vr, vl);
        else
            pki.value = vl;
    }

    private Pair lookup( String vr )
    {
        for( Pair pk : scope )
            if( pk.name.equals(vr) )
                return pk;
        return null;
    }

    public Value get(String var )
    {
        Pair pri = lookup(var);
        return pri != null ? pri.value : null;
    }
}
