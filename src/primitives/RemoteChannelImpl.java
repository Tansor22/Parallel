package primitives;

import java.util.function.Supplier;

public class RemoteChannelImpl<T>  extends Channel<T> implements RemoteChannel<T> {
    @Override
    public T get() {
        return super.get();
    }

    @Override
    public void put(Supplier<T> data) {
        super.put(data);
    }
}
