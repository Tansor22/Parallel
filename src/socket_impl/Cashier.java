package socket_impl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import static threads_impl.Utils.quoted;
import static threads_impl.Utils.say;

public class Cashier extends Server {
    private static Host port = Host.CASHIER;
    private static Host assistantPort = Host.ASSISTANT;
    private static Host issuingPointPort = Host.ISSUING_POINT;

    public Cashier(Host port) {
        super(port);
    }

    public static void main(String[] args) throws IOException {
        new Cashier(Host.CASHIER);
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
            if (isSold()) {
                ObjectOutputStream customerOut = Utils.out(customerSocket);
                Utils.send(customerOut, "#sold");
            } else {
                // not sold
                say("Waiting for assistant...");
                // send the book
                Socket assistantSocket = Utils.getSocket(assistantPort);
                ObjectOutputStream assistantOut = Utils.out(assistantSocket);
                Utils.send(assistantOut, desiredBook);

                // waits for assistants reply
                ObjectInputStream assistantIn = Utils.in(assistantSocket);
                String condition = Utils.receive(assistantIn);
                if ("#ok".equalsIgnoreCase(condition)) {
                    say("Waiting for issuing point...");
                    Socket issuingPointSocket = Utils.getSocket(issuingPointPort);
                    ObjectInputStream issuingPoint = Utils.in(issuingPointSocket);
                    // wait for issuing point
                    boolean wasSold = Utils.receive(issuingPoint).equalsIgnoreCase("true");
                    if (wasSold) {
                        say("The " + quoted(desiredBook) + " has been sold!");
                    }
                }
            }
            // exit
        }
    }

    private boolean isSold() {
        return false;
    }
}
