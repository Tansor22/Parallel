package patterns;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BoundedBuffer<T> {
    private final Lock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    private final T[] items;
    private int _putPtr, _takePtr, _count;

    public BoundedBuffer(int size) {
        items = (T[]) new Object[size];
    }

    public void put(T x) {
        // allows use put and take code only to one process simultaneously
        lock.lock();
        try {
            // cannot fill if it is empty
            while (_count == items.length)
                notFull.await();
            items[_putPtr] = x;
            if (++_putPtr == items.length) _putPtr = 0;
            ++_count;
            // if filled signal all consumers giving permission to take
            notEmpty.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public T take() {
        lock.lock();
        try {
            // cannot take until it is empty
            while (_count == 0)
                notEmpty.await();
            T x =  items[_takePtr];
            if (++_takePtr == items.length) _takePtr = 0;
            --_count;
            // since it is empty signal to all producers giving permission to put
            notFull.signal();
            return x;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        // only in case of throwing an exception
        return null;
    }
}
