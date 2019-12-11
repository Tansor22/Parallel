package socket_impl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static threads_impl.Utils.quoted;
import static threads_impl.Utils.say;

public class Customer extends Server {

    public Customer(Host port) {
        super(port);
    }

    public static void main(String[] args) throws IOException {
        Customer host = new Customer(Host.CUSTOMER);
        //ConcurrencyUtils.createInfiniteThread(host::go, )
    }

    @Override
    protected void go() throws IOException {

        while (true) {
            String randomBook = threads_impl.Utils.getRandomBook();
            say("Waiting for cashier...");
            Socket cashierSocket = Utils.getSocket(Host.CASHIER);

            // sending the book
            ObjectOutputStream cashierOut = Utils.out(cashierSocket);
            Utils.send(cashierOut, randomBook);
            say("I'd like a " + quoted(randomBook) + "book.");

            // waiting for cashier to say ok or no
            say("Is " + quoted(randomBook) + " ok?");
            ObjectInputStream cashierIn = Utils.in(cashierSocket);
            String reply = Utils.receive(cashierIn);
            if (Metadata.NOT_SOLD.equalsIgnoreCase(reply)) {
                // waiting for reply condition or no
                reply = Utils.receive(cashierIn);
                if (Metadata.CONDITION.equalsIgnoreCase(reply)) {
                    // everything is ok
                    say("Waiting for issuing point...");
                    Socket issuingPointSocket = socket.accept();
                    ObjectInputStream issuingPointIn = Utils.in(issuingPointSocket);
                    ObjectOutputStream issuingPointOut = Utils.out(issuingPointSocket);

                    // receive the book
                    String theSameBook = Utils.receive(issuingPointIn);
                    // send money
                    Utils.send(issuingPointOut, Double.toString(Math.random() * 100));
                    // exit
                    say("I've got a " + quoted(theSameBook) + " book now!");
                }
            }
        }
    }
}
