package socket_impl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static threads_impl.Utils.quoted;
import static threads_impl.Utils.say;

public class Assistant extends Server {
    private Host issuingPoint = Host.ISSUING_POINT;
    private Host shelf = Host.SHELF;

    public Assistant(Host port) {
        super(port);
    }

    public static void main(String[] args) throws IOException {
       new Assistant(Host.ASSISTANT).go();
    }

    @Override
    protected void go() throws IOException {
        while (true) {
            say("Wasting for cashier...");
            Socket cashierSocket = socket.accept();

            // request has been received
            ObjectInputStream cashierIn = Utils.in(cashierSocket);
            String book = Utils.receive(cashierIn);

            say("Cashier requested a " + quoted(book) + " book.");

            // connect with shelf
            Socket shelfSocket = Utils.getSocket(shelf);
            // multiplex
            ObjectOutputStream shelfOut = Utils.out(shelfSocket);
            ObjectInputStream shelfIn = Utils.in(shelfSocket);

            // send book
            Utils.send(shelfOut, book);
            // receive the book
            String theSameBook = Utils.receive(shelfIn);
            say("Checking the book...");
            // if in condition
            if (!theSameBook.equalsIgnoreCase("#damaged")) {
                say("The " + quoted(theSameBook) + " in condition!");
                // take to issuing point
                Socket issuingPointSocket = Utils.getSocket(issuingPoint);
                ObjectOutputStream issuingPointOut = Utils.out(issuingPointSocket);
                Utils.send(issuingPointOut, theSameBook);
            } else {
                // as a variant give issuing point #damaged value to send cashier wasSold = false
                // or cashier must wait forassistant to say oke or no then wasits issuing point
            }
            // exit
        }
    }
}
