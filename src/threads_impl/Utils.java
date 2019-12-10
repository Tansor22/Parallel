package threads_impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Utils {
    private final static List<String> BOOKS = new ArrayList<>();

    static {
        BOOKS.add("Sweeney Todd. Demon barber of Flit Street");
        BOOKS.add("Groovy in Action");
        BOOKS.add("Java. Effective programming");
        BOOKS.add("PHP. Cookbook");
        BOOKS.add("The Strange Case Of Dr. Jekyll And Mr. Hyde");
    }

    public static List<String> getBooks() {
        return BOOKS;
    }

    public static <T> T coin(T... args) {
        return args[(int) (Math.random() * args.length)];
    }

    public static String getRandomBook() {
        // min + r * (max - min)
        return BOOKS.get((int) (0 + (BOOKS.size() - 1) * Math.random()));
    }

    public static void say(String message) {
        String threadName = Thread.currentThread().getName();
        DateFormat df = new SimpleDateFormat("HH:mm::ss");
        System.out.println(df.format(Calendar.getInstance().getTime()) + " -> " + threadName + " says: " + message);
    }

    public static String quoted(String message) {
        return "\'" + message + "\'";
    }
}
