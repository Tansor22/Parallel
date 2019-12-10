package socket_impl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static threads_impl.Utils.quoted;
import static threads_impl.Utils.say;

public class Cashier extends Server {
    public Cashier(Host port) {
        super(port);
    }

    public static void main(String[] args) throws IOException {
        new Cashier(Host.CASHIER).go();
    }

    @Override
    protected void go() throws IOException {
        while (true) {
            say("Waiting for customer...");
            Socket customerSocket = socket.accept();

            // request has been received
            ObjectInputStream customerIn = Utils.in(customerSocket);
            String desiredBook = Utils.receive(customerIn);

            // check if not sold
            ObjectOutputStream customerOut = Utils.out(customerSocket);
            if (isSold()) {
                Utils.send(customerOut, Metadata.SOLD);
            } else {
                // not sold
                Utils.send(customerOut, Metadata.NOT_SOLD);
                say("Waiting for assistant...");
                // send the book
                Socket assistantSocket = Utils.getSocket(Host.ASSISTANT);
                ObjectOutputStream assistantOut = Utils.out(assistantSocket);
                Utils.send(assistantOut, desiredBook);

                // waits for assistants reply
                say("Waiting for assistant's reply whether " + quoted(desiredBook) + " in condition or no...");
                ObjectInputStream assistantIn = Utils.in(assistantSocket);
                String condition = Utils.receive(assistantIn);

                if (Metadata.CONDITION.equalsIgnoreCase(condition)) {
                    // absent
                    Utils.send(customerOut, Metadata.CONDITION);
                    say(quoted(desiredBook) + " is in condition! Transfer it to issuing point..");

                    Socket issuingPointSocket = Utils.getSocket(Host.ISSUING_POINT);
                    ObjectInputStream issuingPoint = Utils.in(issuingPointSocket);

                    say("Waiting for issuing point...");
                    String reply = Utils.receive(issuingPoint);
                    if (Metadata.SOLD.equalsIgnoreCase(reply)) {
                        say("The " + quoted(desiredBook) + " has been sold!");
                    }
                } else {
                    // not in condition
                    say("Excuse us, but " + quoted(desiredBook) + " has been damaged.");
                    Utils.send(customerOut, Metadata.NOT_CONDITION);
                }
            }
            // exit
        }
    }

    private boolean isSold() {
        return false;
    }
}
