package patterns;

import java.util.concurrent.locks.Lock;
import java.util.function.BooleanSupplier;
import java.util.function.IntSupplier;
import java.util.function.Supplier;


public class Utils {
    public static int capture(Lock lock, IntSupplier supplier) {
        lock.lock();
        try {
            return supplier.getAsInt();
        } finally {
            lock.unlock();
        }
    }
    public static boolean capture(Lock lock, BooleanSupplier supplier) {
        lock.lock();
        try {
            return supplier.getAsBoolean();
        } finally {
            lock.unlock();
        }
    }
    public static <T> T capture(Lock lock, Supplier<T> supplier) {
        lock.lock();
        try {
            return supplier.get();
        } finally {
            lock.unlock();
        }
    }
    public static int getRandomInt(int a, int b) {
        return a + (int) (Math.random() * (b - a));
    }
    public static int getRandomInt(int b) {
        return getRandomInt(0, b);
    }
}
