package rmi_impl;

import threads_impl.Utils;
import primitives.RemoteChannel;

import java.io.Serializable;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.function.Supplier;

public class Shelf {
    public static void main(String[] args) throws Exception {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        Registry registry = LocateRegistry.getRegistry("127.0.0.1");
        RemoteChannel<String> shelfChannel = (RemoteChannel<String>) registry.lookup("shelfChannel");
        RemoteChannel<String> reverseShelfChannel = (RemoteChannel<String>) registry.lookup("reverseShelfChannel");

        // algo
        Thread.currentThread().setName("Shelf");
        // get book
        while (true) {
            String book = shelfChannel.get();
            // need to be filled
            // check if book exist
            // otherwise useless
            Utils.say("Gave condition");


            // NO
            // coin throw in condition or no
            // get book in condition or no
            reverseShelfChannel.put((Supplier<String> & Serializable) () -> book);
        }
    }
}
