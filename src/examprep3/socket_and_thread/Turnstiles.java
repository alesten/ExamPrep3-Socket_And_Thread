package examprep3.socket_and_thread;

import java.util.concurrent.atomic.AtomicInteger;

public class Turnstiles {

    private AtomicInteger count;
    private int id;

    public Turnstiles(int id) {
        count = new AtomicInteger(0);
        this.id = id;
    }
    
    public void addToCount(){
        count.addAndGet(1);
    }

    public long getCount() {
        return Long.parseLong(count.toString());
    }
}
