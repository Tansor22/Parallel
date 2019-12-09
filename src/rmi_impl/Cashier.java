package rmi_impl;

import primitives.RemoteChannel;

import java.io.Serializable;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.function.Supplier;

import static threads_impl.Utils.quoted;
import static threads_impl.Utils.say;

public class Cashier {
    public static void main(String[] args) throws Exception {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        Registry registry = LocateRegistry.getRegistry("127.0.0.1");
        RemoteChannel<String> dataBookChannel = (RemoteChannel<String>) registry.lookup("dataBookChannel");
        RemoteChannel<String> customerBookChannel = (RemoteChannel<String>) registry.lookup("customerBookChannel");
        RemoteChannel<Boolean> wasSoldChannel = (RemoteChannel<Boolean>) registry.lookup("wasSoldChannel");

        Thread.currentThread().setName("Cashier");
        while (true) {
            // algo
            String desiredBook = customerBookChannel.get();
            // check if not sold
            // then
            dataBookChannel.put((Supplier<String> & Serializable) () -> desiredBook);

            // wait for issuing point
            Boolean wasSold = wasSoldChannel.get();
            if (wasSold) {
                say("The " + quoted(desiredBook) + " has been sold!");
            }
            // exit
        }
    }
}
