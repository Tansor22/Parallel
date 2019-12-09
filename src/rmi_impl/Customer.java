package rmi_impl;

import threads_impl.Utils;
import primitives.RemoteChannel;

import java.io.Serializable;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.function.Supplier;

import static threads_impl.Utils.quoted;
import static threads_impl.Utils.say;

public class Customer {
//    static MappedChannel customerBookChannel = new MappedChannel("customerBookChannel");
//    static MappedChannel issuingPointCustomerChannel = new MappedChannel("issuingPointCustomerChannel");
//    static MappedChannel payingChannel = new MappedChannel("payingChannel");
    public static void main(String[] args) throws Exception {
       /* String randomBook = Utils.getRandomBook();
        customerBookChannel.put(() -> randomBook);
        say("I'd like a " + quoted(randomBook) + "book.");

        String theSameBook = issuingPointCustomerChannel.get();
        payingChannel.put(() -> Double.toString(Math.random() * 100));

        say("I've got a " + quoted(theSameBook) + " book now!");*/
       // meta configuration
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        Registry registry = LocateRegistry.getRegistry("127.0.0.1");
        RemoteChannel<String> customerBookChannel = (RemoteChannel<String>)  registry.lookup("customerBookChannel");
        RemoteChannel<String> issuingPointCustomerChannel = (RemoteChannel<String>)  registry.lookup("issuingPointCustomerChannel");
        RemoteChannel<Double> payingChannel = (RemoteChannel<Double>)  registry.lookup("payingChannel");

        // algo
        Thread.currentThread().setName("Customer");
        while (true) {
            String randomBook = Utils.getRandomBook();
            customerBookChannel.put((Supplier<String> & Serializable) () -> randomBook);
            say("I'd like a " + quoted(randomBook) + " book.");
            String theSameBook = issuingPointCustomerChannel.get();
            payingChannel.put((Supplier<Double> & Serializable) () -> Math.random() * 100);

            say("I've got a " + quoted(theSameBook) + " book now!");
        }
    }
}
