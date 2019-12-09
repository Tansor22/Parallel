package primitives;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.function.Supplier;

public interface RemoteChannel<T> extends Remote {
    void put(Supplier<T> data) throws RemoteException;
    T get() throws RemoteException;
}
