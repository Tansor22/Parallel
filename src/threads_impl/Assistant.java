package threads_impl;

import primitives.Channel;

import static threads_impl.Utils.quoted;
import static threads_impl.Utils.say;

public class Assistant implements Runnable {
    private Channel<String> dataBookChannel;
    private Channel<String> shelfChannel;
    private Channel<String> reverseShelfChannel;
    private Channel<String> assistantIssuingPointChannel;

    public Assistant(Channel<String> dataBookChannel, Channel<String> shelfChannel, Channel<String> reverseShelfChannel, Channel<String> assistantIssuingPointChannel) {
        this.dataBookChannel = dataBookChannel;
        this.shelfChannel = shelfChannel;
        this.reverseShelfChannel = reverseShelfChannel;
        this.assistantIssuingPointChannel = assistantIssuingPointChannel;
    }

    @Override
    public void run() {
        String book = dataBookChannel.get();
        say("Cashier requested a " + quoted(book) + " book.");

        // check shelf
        shelfChannel.put(() -> book);
        String theSameBook = reverseShelfChannel.get();
        say("Checking the book...");
        // if in condition
        say("The " + quoted(theSameBook) + " in condition!");
        // take to issuing point
        assistantIssuingPointChannel.put(() -> theSameBook);

        // exit

    }
}
