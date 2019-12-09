package runners;

import primitives.BiSemaphore;
import primitives.MySemaphore;

import java.util.concurrent.Semaphore;

public class SemaphoresDemo {
    public static void main(String[] args) {
        int permits = 2;
        // BI
        BiSemaphore bi = new BiSemaphore(1);
        // Java Semaphore
        Semaphore jSem = new Semaphore(2, true);
        MySemaphore sem = new MySemaphore(permits, 4);
        //sem = new JavaSemaphoreAdapter(permits, permits);
        new Philosopher(sem, "Сократ").start();
        new Philosopher(sem, "Платон").start();
        new Philosopher(sem, "Аристотель").start();
        new Philosopher(sem, "Фалес").start();
        new Philosopher(sem, "Пифагор").start();

    }


}
