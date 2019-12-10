package socket_impl;

import java.io.IOException;
import java.net.ServerSocket;

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
    protected abstract void go() throws IOException;
}
