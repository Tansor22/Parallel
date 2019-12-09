package threads_impl;

import utils.ConcurrencyUtils;
import primitives.Channel;

import java.util.ArrayList;
import java.util.List;

import static utils.ConcurrencyUtils.*;
public class App {
    public static void main(String[] args) {
        // Meta
        List<Thread> threads = new ArrayList<>();
        Channel<String> dataBookChannel = new Channel<>();
        Channel<String> customerBookChannel = new Channel<>();
        Channel<String> issuingPointCustomerChannel = new Channel<>();
        Channel<Double> payingChannel = new Channel<>();
        Channel<Boolean> wasSoldChannel = new Channel<>();
        Channel<String> shelfChannel = new Channel<>();
        Channel<String> reverseShelfChannel = new Channel<>();
        Channel<String> assistantIssuingPointChannel = new Channel<>();

        // System Objects
        Runnable customer = new Customer(customerBookChannel, issuingPointCustomerChannel, payingChannel);
        Runnable cashier = new Cashier(customerBookChannel, dataBookChannel, wasSoldChannel);
        Runnable assistant = new Assistant(dataBookChannel, shelfChannel, reverseShelfChannel, assistantIssuingPointChannel);
        Runnable shelf = new Shelf(shelfChannel, reverseShelfChannel);
        Runnable issuingPoint = new IssuingPoint(assistantIssuingPointChannel, issuingPointCustomerChannel, wasSoldChannel, payingChannel);

        threads.add(createInfiniteThread(customer, "Customer", 2.5));
        threads.add(createInfiniteThread(cashier, "Cashier", 2.5));
        threads.add(createInfiniteThread(assistant, "Assistant", 2.5));
        threads.add(createInfiniteThread(shelf, "Shelf", 2.5));
        threads.add(createInfiniteThread(issuingPoint, "Issuing Point", 2.5));

        // Start
        ConcurrencyUtils.startMultipleThreadsMessy(threads);
    }
}
