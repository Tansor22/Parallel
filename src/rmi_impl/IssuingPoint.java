package rmi_impl;

import primitives.RemoteChannel;

import java.io.Serializable;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.function.Supplier;

import static threads_impl.Utils.quoted;
import static threads_impl.Utils.say;

public class IssuingPoint {
    public static void main(String[] args) throws Exception{
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        Registry registry = LocateRegistry.getRegistry("127.0.0.1");
        RemoteChannel<String> assistantIssuingPointChannel = (RemoteChannel<String>)  registry.lookup("assistantIssuingPointChannel");
        RemoteChannel<String> issuingPointCustomerChannel =  (RemoteChannel<String>)  registry.lookup("issuingPointCustomerChannel");
        RemoteChannel<Boolean> wasSoldChannel = (RemoteChannel<Boolean>)  registry.lookup("wasSoldChannel");
        RemoteChannel<Double> payingChannel = (RemoteChannel<Double>)  registry.lookup("payingChannel");

        Thread.currentThread().setName("IssuingPoint");
        // getBook from Assistant
        while (true) {
            String book = assistantIssuingPointChannel.get();
            say("An assistant gave us a " + quoted(book) + " book.");

            // packBook
            say("Packing the book...");

            // put to Customer
            issuingPointCustomerChannel.put((Supplier<String> & Serializable) () -> book);
            // getMoney
            Double money = payingChannel.get();
            // mark as sold
            say("We've earned " + money + "$!");
            // put sold Book to Cashier
            wasSoldChannel.put((Supplier<Boolean> & Serializable) () -> true);
        }
    }
}
