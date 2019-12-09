package primitives;

import java.util.concurrent.Semaphore;

public class JavaSemaphoreAdapter extends MySemaphore {
    private Semaphore innerSem;

    public JavaSemaphoreAdapter(int count, int maxCount) {
        super(count, maxCount);
        innerSem = new Semaphore(count);
    }

    @Override
    public void capture() {
        try {
            innerSem.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void release() {
        innerSem.release();
    }
}
