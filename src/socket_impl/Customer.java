package socket_impl;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static threads_impl.Utils.quoted;
import static threads_impl.Utils.say;

public class Customer extends Server {
    private static int denials;

    public Customer(Host port) {
        super(port);
    }

    public static void main(String[] args) {
        new Customer(Host.CUSTOMER).go();

        // condition for exit: random or amount of denials is above
        // sounds interesting but hard to implement
        // need to implement probability coin
        /*Customer host = new Customer(Host.CUSTOMER);
        ConcurrencyUtils.createInfiniteThread(host::go, Host.CUSTOMER.name(), () -> coin(true, false) || denials > 3)
                .start();*/
    }

    @Override
    protected void go() {
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
                    Socket issuingPointSocket = accept();
                    ObjectInputStream issuingPointIn = Utils.in(issuingPointSocket);
                    ObjectOutputStream issuingPointOut = Utils.out(issuingPointSocket);

                    // receive the book
                    String theSameBook = Utils.receive(issuingPointIn);
                    // send money
                    Utils.send(issuingPointOut, Double.toString(Math.random() * 100));
                    // exit
                    say("I've got a " + quoted(theSameBook) + " book now!");
                } else denials++;
            } else denials++;
            // increment counter of denials in case of deny
        }
    }
}
