package examprep3.socket_and_thread;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

public class TurnstilesControl {

    Map<Integer, Turnstiles> turnstiles;

    public TurnstilesControl() {
        turnstiles = new HashMap();
    }

    public boolean registerTurnstile(int id) {
        if (turnstiles.containsKey(id)) {
            return false;
        }

        turnstiles.put(id, new Turnstiles(id));
        return true;
    }

    public long getTurnstileCount(int id) {
        if (!turnstiles.containsKey(id)) {
            return -1;
        }

        return turnstiles.get(id).getCount();
    }
    
    public long getTurnstilesCount(){
        long sum = 0;
        
        for (Map.Entry<Integer, Turnstiles> entrySet : turnstiles.entrySet()) {
            Turnstiles t = entrySet.getValue();
            sum += t.getCount();
        }
        
        return sum;
    }
    
    public boolean incrementTurnstile(int id){
        if (!turnstiles.containsKey(id)) {
            return false;
        }
        
        Turnstiles t = turnstiles.get(id);
        t.addToCount();
        turnstiles.put(id, t);
        
        return true;
    }
}
