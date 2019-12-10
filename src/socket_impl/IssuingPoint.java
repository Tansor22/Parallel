package socket_impl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import static threads_impl.Utils.quoted;
import static threads_impl.Utils.say;

public class IssuingPoint {
    private static ServerSocket itsSocket;
    private static Socket assistantSocket;
    private static Socket customerSocket;
    private static Socket cashierSocket;


    private static Host port = Host.ISSUING_POINT;
    private static Host customerPort = Host.CUSTOMER;
    private static Host cashierPort = Host.CASHIER;

    public static void main(String[] args) throws IOException {
        itsSocket = new ServerSocket(port.port);
        customerSocket = Utils.getSocket(customerPort);
        cashierSocket = Utils.getSocket(cashierPort);

        while (true) {
            assistantSocket = itsSocket.accept();
            ObjectInputStream assistantIn = Utils.in(assistantSocket);
            String book = Utils.receive(assistantIn);

            say("An assistant gave us a " + quoted(book) + " book.");

            // packBook
            say("Packing the book...");

            // put to Customer
            ObjectOutputStream customerOut = Utils.out(customerSocket);
            ObjectInputStream customerIn = Utils.in(customerSocket);
            Utils.send(customerOut, book);
            // getMoney
            double money = Double.parseDouble(Utils.receive(customerIn));
            // mark as sold
            say("We've earned " + money + "$!");
            // put sold Book to Cashier
            ObjectOutputStream cashierOut = Utils.out(cashierSocket);

            Utils.send(cashierOut, "true");
        }
    }
}
