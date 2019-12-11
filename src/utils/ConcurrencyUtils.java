package utils;

import java.util.*;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Supplier;

public class ConcurrencyUtils {
    public static void waitForMultipleThreads(Thread... threads) {
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void waitForMultipleThreads(List<Thread> threads) {
        waitForMultipleThreads(threads.toArray(new Thread[]{}));
    }

    public static Thread createThread(Runnable r) {
        return new Thread(r);
    }

    public static Thread createThread(Runnable r, String threadName) {
        Thread thread = new Thread(r);
        thread.setName(threadName);
        return thread;
    }

    public static Thread createInfiniteThread(Runnable r, String threadName) {
        return createInfiniteThread(r, threadName, .5, () -> true);
    }
    public static Thread createInfiniteThread(Runnable r, String threadName, BooleanSupplier sup) {
        return createInfiniteThread(r, threadName, .5, sup);
    }

    public static Thread createInfiniteThread(Runnable r, String threadName, double period, BooleanSupplier sup) {
        Function<Double, Long> secondsConverter = doubleSeconds -> (long) (doubleSeconds * 1000);
        Thread thread = new Thread(() -> {
            while (sup.getAsBoolean()) {
                r.run();
                try {
                    Thread.sleep(secondsConverter.apply(period));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.setName(threadName);
        return thread;
    }

    public static Thread createInfiniteThread(Runnable r, String threadName, double period, double delay) {
        Function<Double, Long> secondsConverter = doubleSeconds -> (long) (doubleSeconds * 1000);
        Thread thread = new Thread(() -> {
            while (true) {
                r.run();
                try {
                    Thread.sleep(secondsConverter.apply(period));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }) {
            @Override
            public synchronized void start() {
                try {
                    Thread.sleep(secondsConverter.apply(delay));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                super.start();
            }
        };
        thread.setName(threadName);
        return thread;
    }

    public static void startMultipleThreads(List<Thread> threads) {
        startMultipleThreads(threads.toArray(new Thread[]{}));
    }

    public static void startMultipleThreadsMessy(List<Thread> threads) {
        Collections.shuffle(threads);
        startMultipleThreads(threads);
    }

    public static void startMultipleThreadsMessy(List<Thread>... threadsLists) {
        List<Thread> res = new ArrayList<>();
        for (List<Thread> threads : threadsLists)
            res.addAll(threads);
        Collections.shuffle(res);
        startMultipleThreads(res);
    }

    public static void startMultipleThreads(Thread... threads) {
        for (Thread thread : threads)
            thread.start();
    }

    public static void startAndWaitMultipleThreads(Thread... threads) {
        startMultipleThreads(threads);
        waitForMultipleThreads(threads);
    }

    public static void startAndWaitMultipleThreads(List<Thread> threads) {
        startMultipleThreads(threads);
        waitForMultipleThreads(threads);
    }

    public static void sleep(double seconds) {
        Function<Double, Long> secondsConverter = doubleSeconds -> (long) (doubleSeconds * 1000);
        try {
            Thread.sleep(secondsConverter.apply(seconds));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sleep(String message, double seconds) {
        say(message);
        sleep(seconds);
    }

    public static void say(String message) {
        say(message, 0);
    }

    public static void say(String message, int indentSize) {
        Supplier<String> messageSupplier = () -> {
            String threadName = Thread.currentThread().getName();
            StringBuilder sb = new StringBuilder();
            if (indentSize > 0)
                for (int i = 0; i < indentSize; i++)
                    sb.append('\t');
            sb.append(threadName).append(" says: ").append(message);
            return sb.toString();
        };
        System.out.println(messageSupplier.get());
    }

    public static void say(String message, double seconds) {
        say(message);
        sleep(seconds);
    }
}
