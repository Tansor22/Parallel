package primitives;

public class MySemaphore {
    private volatile int count;
    private int maxCount;

    public MySemaphore(int count, int maxCount) {
        this.count = count;
        this.maxCount = maxCount;
    }
    public MySemaphore() {}
    // P()
    public void capture() {
        while (count == 0);
        // Non-atomic op on volatile var
        count--;
    }

    // V()
    public void release() {
        // Non-atomic op on volatile var
        count++;
        if (count > maxCount) count = maxCount;
    }
}
