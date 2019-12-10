package socket_impl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static threads_impl.Utils.quoted;
import static threads_impl.Utils.say;

public class Assistant extends Server {
    public Assistant(Host port) {
        super(port);
    }

    public static void main(String[] args) throws IOException {
        new Assistant(Host.ASSISTANT).go();
    }

    @Override
    protected void go() throws IOException {
        while (true) {
            say("Waiting for cashier...");
            Socket cashierSocket = socket.accept();

            // request has been received
            ObjectInputStream cashierIn = Utils.in(cashierSocket);
            ObjectOutputStream cashierOut = Utils.out(cashierSocket);
            String book = Utils.receive(cashierIn);

            say("Cashier requested a " + quoted(book) + " book.");

            // connect with shelf
            Socket shelfSocket = Utils.getSocket(Host.SHELF);
            // multiplex
            ObjectOutputStream shelfOut = Utils.out(shelfSocket);
            ObjectInputStream shelfIn = Utils.in(shelfSocket);

            // send book
            Utils.send(shelfOut, book);
            // receive the book
            String reply = Utils.receive(shelfIn);
            say("Checking the book...");
            // if in condition
            if (Metadata.CONDITION.equalsIgnoreCase(reply)) {
                say("The " + quoted(book) + " in condition!");
                // send to cashier
                Utils.send(cashierOut, reply);
                // take to issuing point
                Socket issuingPointSocket = Utils.getSocket(Host.ISSUING_POINT);
                ObjectOutputStream issuingPointOut = Utils.out(issuingPointSocket);
                Utils.send(issuingPointOut, book);
            } else {
                // not condition
                Utils.send(cashierOut, reply);
            }
            // exit
        }
    }
}
