package primitives;

import java.util.function.Supplier;

public class Channel<T> {
    private MySemaphore free = new BiSemaphore();
    private MySemaphore empty = new BiSemaphore();
    private T data;

    public Channel() {
        empty.capture();
    }

    public void put(Supplier<T> data) {
        // String threadName = Thread.currentThread().getName();
        // System.out.println(threadName + " has entered set() method.");
        free.capture();
        // System.out.println(threadName + " has captured free sem.");
        this.data = data.get();
        // System.out.println(threadName + " has finished data processing.");
        empty.release();
        // System.out.println(threadName + " has released empty sem.");
    }

    public T get() {
        // String threadName = Thread.currentThread().getName();
        // System.out.println(threadName + " has entered get() method.");
        empty.capture();
        // System.out.println(threadName + " has captured empty sem.");
        free.release();
        // System.out.println(threadName + " has released free sem.");
        return data;
    }
}
