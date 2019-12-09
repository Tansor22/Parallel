package threads_impl;

import primitives.Channel;

import static threads_impl.Utils.*;


public class Customer implements Runnable {
    private Channel<String> customerBookChannel;
    private Channel<String> issuingPointCustomerChannel;
    private Channel<Double> payingChannel;

    public Customer(Channel<String> customerBookChannel, Channel<String> issuingPointCustomerChannel, Channel<Double> payingChannel) {
        this.customerBookChannel = customerBookChannel;
        this.issuingPointCustomerChannel = issuingPointCustomerChannel;
        this.payingChannel = payingChannel;
    }

    @Override
    public void run() {
        String randomBook = Utils.getRandomBook();
        customerBookChannel.put(() -> randomBook);
        say("I'd like a " + quoted(randomBook) + "book.");
        // waiting on issuing point and so on
        String theSameBook = issuingPointCustomerChannel.get();
        // paying
        payingChannel.put(() -> Math.random() * 100);
        // exit
        say("I've got a " + quoted(theSameBook) + " book now!");
    }
}