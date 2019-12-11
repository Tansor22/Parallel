package socket_impl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import static threads_impl.Utils.say;

public class Utils {
    public static String receive(ObjectInputStream in) {
        String output = null;
        try {
            output = (String) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            return output;
        }
    }

    public static void send(ObjectOutputStream out, String data) {
        try {
            out.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ObjectInputStream in(Socket socket) {
        try {
            return new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ObjectOutputStream out(Socket socket) {
        try {
            return new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static Socket getSocket(Host port) {
        Socket output = null;
        while (output == null) {
            try {
                output = new Socket(InetAddress.getLocalHost().getHostName(), port.port);
            } catch (IOException e) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
               say("Waiting for " + port.name() + "...");
            }
        }
        return output;
    }
}
