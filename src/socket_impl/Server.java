package socket_impl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class Server {
    protected Host port;
    protected ServerSocket socket;

    public Server(Host port) {
        this.port = port;
        try {
            socket = new ServerSocket(port.port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Thread.currentThread().setName(port.name());
    }

    protected Socket accept() {
        Socket output = null;
        try {
            output = socket.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;
    }

    protected abstract void go() throws IOException;
}
