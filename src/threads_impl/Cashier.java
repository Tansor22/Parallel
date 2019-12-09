package threads_impl;

import primitives.Channel;
import static threads_impl.Utils.quoted;
import static threads_impl.Utils.say;

public class Cashier implements Runnable {
    private Channel<String> dataBookChannel;
    private Channel<String> customerBookChannel;
    private Channel<Boolean> wasSoldChannel;

    public Cashier(Channel<String> customerBookChannel, Channel<String> dataBookChannel, Channel<Boolean> wasSoldChannel) {
        this.dataBookChannel = dataBookChannel;
        this.customerBookChannel = customerBookChannel;
        this.wasSoldChannel = wasSoldChannel;
    }

    @Override
    public void run() {
        String desiredBook = customerBookChannel.get();
        // check if not sold
        // then
        dataBookChannel.put(() -> desiredBook);

        // wait for issuing point
        Boolean wasSold = wasSoldChannel.get();
        if (wasSold) {
            say("The " + quoted(desiredBook) + " has been sold!");
        }
        // exit
    }
}
