package socket_impl;

public enum Host {
    CUSTOMER(9876),
    CASHIER(9875),
    ASSISTANT(9874),
    SHELF(9873),
    ISSUING_POINT(9872);
    int port;

    Host(int port) {
        this.port = port;
    }
}
