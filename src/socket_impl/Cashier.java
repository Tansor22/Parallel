package socket_impl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import static threads_impl.Utils.quoted;
import static threads_impl.Utils.say;

public class Cashier {
    private static ServerSocket itsSocket;
    private static Socket customerSocket;
    private static Socket issuingPointSocket;
    private static Socket assistantSocket;

    private static Port port = Port.CASHIER;
    private static Port customerPort = Port.CUSTOMER;
    private static Port assistantPort = Port.ASSISTANT;
    private static Port issuingPointPort = Port.ISSUING_POINT;

    public static void main(String[] args) throws IOException {
        itsSocket = new ServerSocket(port.port);
        assistantSocket = Utils.getSocket(assistantPort);
        issuingPointSocket = Utils.getSocket(issuingPointPort);

        while (true) {
            customerSocket = itsSocket.accept();
            ObjectInputStream customerIn = Utils.in(customerSocket);
            String desiredBook = Utils.receive(customerIn);

            // check if not sold
            // then
            ObjectOutputStream assistantOut = Utils.out(assistantSocket);
            Utils.send(assistantOut, desiredBook);

            ObjectInputStream issuingPoint = Utils.in(issuingPointSocket);
            // wait for issuing point
            boolean wasSold = Utils.receive(issuingPoint).equalsIgnoreCase("true");
            if (wasSold) {
                say("The " + quoted(desiredBook) + " has been sold!");
            }
            // exit

        }
    }
}
