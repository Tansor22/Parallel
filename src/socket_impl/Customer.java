package socket_impl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import static threads_impl.Utils.quoted;
import static threads_impl.Utils.say;

public class Customer {
    private static ServerSocket itsSocket;
    private static Socket cashierSocket;
    private static Socket issuingPointSocket;

    private static Host port = Host.CUSTOMER;

    private static Host cashierPort = Host.CASHIER;
    private static Host issuingPointPort = Host.ISSUING_POINT;

    public static void main(String[] args) throws IOException {
        Thread.currentThread().setName(port.name());
        itsSocket = new ServerSocket(port.port);
        cashierSocket = Utils.getSocket(cashierPort);
        //issuingPointSocket = Utils.getSocket(issuingPointPort);
        ObjectOutputStream cashierOut = Utils.out(cashierSocket);

        //ObjectOutputStream issuingPortOut = new ObjectOutputStream(issuingPointSocket.getOutputStream());

        while (true) {
            String randomBook = threads_impl.Utils.getRandomBook();
            Utils.send(cashierOut, randomBook);
            say("I'd like a " + quoted(randomBook) + "book.");
            // waiting on issuing point and so on
            issuingPointSocket = itsSocket.accept();
            ObjectInputStream issuingPointIn = Utils.in(issuingPointSocket);
            ObjectOutputStream issuingPointOut = Utils.out(issuingPointSocket);
            String theSameBook = Utils.receive(issuingPointIn);
            // paying
            Utils.send(issuingPointOut, Double.toString(Math.random() * 100));
            // exit
            say("I've got a " + quoted(theSameBook) + " book now!");
        }
    }
}
