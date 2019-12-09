package socket_impl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import static threads_impl.Utils.quoted;
import static threads_impl.Utils.say;

public class Assistant {
    private static ServerSocket itsSocket;
    private static Socket cashierSocket;
    private static Socket issuingPointSocket;
    private static Socket shelfSocket;

    private static Port port = Port.ASSISTANT;
    private static Port cashierPort = Port.CASHIER;
    private static Port issuingPointPort = Port.ISSUING_POINT;
    private static Port shelfPort = Port.SHELF;

    public static void main(String[] args) throws IOException {
        itsSocket = new ServerSocket(port.port);
        //cashierSocket = Utils.getSocket(cashierPort);
        shelfSocket = Utils.getSocket(shelfPort);
        issuingPointSocket = Utils.getSocket(issuingPointPort);
        while (true) {
            cashierSocket = itsSocket.accept();
            ObjectInputStream cashierIn = Utils.in(cashierSocket);
            String book = Utils.receive(cashierIn);
            say("Cashier requested a " + quoted(book) + " book.");
            // check shelf
            ObjectOutputStream shelfOut = Utils.out(shelfSocket);
            ObjectInputStream shelfIn = Utils.in(shelfSocket);
            Utils.send(shelfOut, book);
            String theSameBook = Utils.receive(shelfIn);
            say("Checking the book...");
            // if in condition
            say("The " + quoted(theSameBook) + " in condition!");
            // take to issuing point
            ObjectOutputStream issuingPointOut = Utils.out(issuingPointSocket);
            Utils.send(issuingPointOut, theSameBook);
            // exit

        }

    }
}
