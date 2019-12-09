package primitives;

public class Signal {
    private volatile boolean wait;

    public void waiting() {
        wait = true;
        while (wait) ;
    }

    public void send() {
        wait = false;
    }
}
