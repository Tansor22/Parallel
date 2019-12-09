package rmi_impl;

import primitives.RemoteChannel;
import primitives.RemoteChannelImpl;

import java.io.Serializable;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Supervisor implements Serializable {
    public static void main(String[] args) {
        new Supervisor().doEverything();

    }
    public void doEverything() {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        // remote objects
        RemoteChannel<String> dataBookChannel = new RemoteChannelImpl<>();
        RemoteChannel<String> customerBookChannel = new RemoteChannelImpl<>();
        RemoteChannel<String> issuingPointCustomerChannel = new RemoteChannelImpl<>();
        RemoteChannel<Double> payingChannel = new RemoteChannelImpl<>();
        RemoteChannel<Boolean> wasSoldChannel = new RemoteChannelImpl<>();
        RemoteChannel<String> shelfChannel = new RemoteChannelImpl<>();
        RemoteChannel<String> reverseShelfChannel = new RemoteChannelImpl<>();
        RemoteChannel<String> assistantIssuingPointChannel = new RemoteChannelImpl<>();

        // binding
        try {
            // stubbing
            RemoteChannel<String> dataBookChannelStub = (RemoteChannel<String>) UnicastRemoteObject.exportObject(dataBookChannel, 0);
            RemoteChannel<String> customerBookChannelStub = (RemoteChannel<String>) UnicastRemoteObject.exportObject(customerBookChannel, 0);
            RemoteChannel<String> issuingPointCustomerChannelStub = (RemoteChannel<String>) UnicastRemoteObject.exportObject(issuingPointCustomerChannel, 0);
            RemoteChannel<Double> payingChannelStub = (RemoteChannel<Double>) UnicastRemoteObject.exportObject(payingChannel, 0);
            RemoteChannel<Boolean> wasSoldChannelStub = (RemoteChannel<Boolean>) UnicastRemoteObject.exportObject(wasSoldChannel, 0);
            RemoteChannel<String> shelfChannelStub = (RemoteChannel<String>) UnicastRemoteObject.exportObject(shelfChannel, 0);
            RemoteChannel<String> reverseShelfChannelStub = (RemoteChannel<String>) UnicastRemoteObject.exportObject(reverseShelfChannel, 0);
            RemoteChannel<String> assistantIssuingPointChannelStub = (RemoteChannel<String>) UnicastRemoteObject.exportObject(assistantIssuingPointChannel, 0);

            // binding
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.bind("dataBookChannel", dataBookChannelStub);
            registry.bind("customerBookChannel", customerBookChannelStub);
            registry.bind("issuingPointCustomerChannel", issuingPointCustomerChannelStub);
            registry.bind("payingChannel", payingChannelStub);
            registry.bind("wasSoldChannel", wasSoldChannelStub);
            registry.bind("shelfChannel", shelfChannelStub);
            registry.bind("reverseShelfChannel", reverseShelfChannelStub);
            registry.bind("assistantIssuingPointChannel", assistantIssuingPointChannelStub);
            System.out.println("All objects are bound.");
            //for(;;) LockSupport.park();
            while (true);
        } catch (Throwable cause) {
            System.out.println("Couldn't bind objects due to " + cause.getCause());
            cause.printStackTrace();
        }
    }
}
