package primitives;

public class BiSemaphore extends MySemaphore{
    private volatile int count;

    public BiSemaphore(int count) {
        super(count, count);
        if (count > 0)
            this.count = 1;
    }

    public BiSemaphore() {
        this.count = 1;
    }


    // P()
    public void capture() {
        while (count == 0);
        count = 0;
    }

    // V()
    public void release() {
        count = 1;
    }

}
