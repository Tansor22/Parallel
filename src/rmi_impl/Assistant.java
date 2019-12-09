package rmi_impl;

import primitives.RemoteChannel;

import java.io.Serializable;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.function.Supplier;

import static threads_impl.Utils.quoted;
import static threads_impl.Utils.say;

public class Assistant {
    public static void main(String[] args) throws Exception {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        Registry registry = LocateRegistry.getRegistry("127.0.0.1");

        RemoteChannel<String> dataBookChannel = (RemoteChannel<String>) registry.lookup("dataBookChannel");
        RemoteChannel<String> shelfChannel = (RemoteChannel<String>) registry.lookup("shelfChannel");
        RemoteChannel<String> reverseShelfChannel = (RemoteChannel<String>) registry.lookup("reverseShelfChannel");
        RemoteChannel<String> assistantIssuingPointChannel = (RemoteChannel<String>) registry.lookup("assistantIssuingPointChannel");

        Thread.currentThread().setName("Assistant");
        // algo
        while (true) {
            String book = dataBookChannel.get();
            say("Cashier requested a " + quoted(book) + " book.");

            // check shelf
            shelfChannel.put((Supplier<String> & Serializable) () -> book);
            String theSameBook = reverseShelfChannel.get();
            say("Checking the book...");
            // if in condition
            say("The " + quoted(theSameBook) + " in condition!");
            // take to issuing point
            assistantIssuingPointChannel.put((Supplier<String> & Serializable) () -> theSameBook);
        }
    }
}
