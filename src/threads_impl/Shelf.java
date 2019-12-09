package threads_impl;

import primitives.Channel;

public class Shelf implements Runnable {
    private Channel<String> shelfChannel;
    private Channel<String> reverseShelfChannel;

    public Shelf(Channel<String> shelfChannel, Channel<String> reverseShelfChannel) {
        this.shelfChannel = shelfChannel;
        this.reverseShelfChannel = reverseShelfChannel;
    }

    @Override
    public void run() {
        // get book
        String book = shelfChannel.get();
        // need to be filled
        // check if book exist
        // otherwise useless

        // NO
        // coin throw in condition or no
        // get book in condition or no
        reverseShelfChannel.put(() -> book);

    }
}
