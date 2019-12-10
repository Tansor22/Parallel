package socket_impl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Shelf {
    private static ServerSocket itsSocket;
    private static Socket assistantSocket;

    private static Host port = Host.SHELF;

    public static void main(String[] args) throws IOException {
        itsSocket = new ServerSocket(port.port);
        while (true) {
            assistantSocket = itsSocket.accept();

            ObjectOutputStream assistantOut = Utils.out(assistantSocket);
            ObjectInputStream assistantIn = Utils.in(assistantSocket);
            String book = Utils.receive(assistantIn);
            // condition or no

            Utils.send(assistantOut, book);
        }
    }
}
