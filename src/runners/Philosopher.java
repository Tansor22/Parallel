package runners;

import primitives.MySemaphore;

public class Philosopher extends Thread {

    private MySemaphore sem;

    // поел ли философ
    private boolean full = false;

    private String name;

    public Philosopher(MySemaphore sem, String name) {
        this.sem = sem;
        this.name = name;
    }

    public void run() {
        try {
            // если философ еще не ел
            if (!full) {
                //Запрашиваем у семафора разрешение на выполнение
                sem.capture();
                System.out.println(name + " садится за стол");

                // философ ест
                sleep(300);
                full = true;

                System.out.println(name + " поел! Он выходит из-за стола");
                sem.release();

                // философ ушел, освободив место другим
                sleep(300);
            }
        } catch (InterruptedException e) {
            System.out.println("Что-то пошло не так!");
        }
    }
}