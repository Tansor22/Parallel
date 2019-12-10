package socket_impl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static threads_impl.Utils.say;

public class Shelf extends Server {
    public Shelf(Host port) {
        super(port);
    }

    public static void main(String[] args) throws IOException {
        new Shelf(Host.SHELF).go();
    }

    @Override
    protected void go() throws IOException {
        while (true) {
            say("Waiting for assistant...");
            Socket assistantSocket = socket.accept();

            ObjectOutputStream assistantOut = Utils.out(assistantSocket);
            ObjectInputStream assistantIn = Utils.in(assistantSocket);
            String book = Utils.receive(assistantIn);
            // condition or no
            // Books map, book -> % of condition

            Utils.send(assistantOut, threads_impl.Utils.coin(Metadata.CONDITION/*, Metadata.NOT_CONDITION*/));
        }
    }
}
