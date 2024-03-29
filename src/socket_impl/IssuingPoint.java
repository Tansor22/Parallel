package socket_impl;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static threads_impl.Utils.quoted;
import static threads_impl.Utils.say;

public class IssuingPoint extends Server {
    public IssuingPoint(Host port) {
        super(port);
    }

    public static void main(String[] args) {
      new IssuingPoint(Host.ISSUING_POINT).go();
    }

    @Override
    protected void go() {
        while (true) {
            say("Waiting for assistant...");
            Socket assistantSocket = accept();

            ObjectInputStream assistantIn = Utils.in(assistantSocket);
            String book = Utils.receive(assistantIn);

            say("An assistant gave us a " + quoted(book) + " book.");

            // packBook
            say("Packing the book...");

            // put sold Book to Cashier
            say("Waiting for cashier...");
            Socket cashierSocket = Utils.getSocket(Host.CASHIER);
            ObjectOutputStream cashierOut = Utils.out(cashierSocket);
            say(quoted(book) + " was sold!");
            // First of all complete all operations cashier issuing point the paying
            Utils.send(cashierOut, Metadata.SOLD);

            // send to Customer
            say("Waiting for customer...");
            Socket customerSocket = Utils.getSocket(Host.CUSTOMER);
            ObjectOutputStream customerOut = Utils.out(customerSocket);
            ObjectInputStream customerIn = Utils.in(customerSocket);
            Utils.send(customerOut, book);
            // getMoney
            // book map, book -> % of condition, price ???
            double money = Double.parseDouble(Utils.receive(customerIn));
            // mark as sold
            say("We've earned " + money + "$!");
        }
    }
}
