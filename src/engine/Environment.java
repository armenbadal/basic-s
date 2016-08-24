package engine;

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

    private List<Pair> scope = null;

    public Environment()
    {
        scope = new ArrayList<>();
    }

    public void add( String vr, Constant vl )
    {
        scope.add(new Pair(vr,vl));
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
        for( Pair pk : scope )
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
