package socket_impl;

public enum Port {
    CUSTOMER(9876),
    CASHIER(9875),
    ASSISTANT(9874),
    SHELF(9873),
    ISSUING_POINT(9872);
    int port;

    Port(int port) {
        this.port = port;
    }
}
