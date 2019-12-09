package threads_impl;

import primitives.Channel;
import static threads_impl.Utils.*;

public class IssuingPoint implements Runnable{
    private Channel<String> assistantIssuingPointChannel;
    private Channel<String> issuingPointCustomerChannel;
    private Channel<Boolean> wasBookSoldChannel;
    private Channel<Double> payingChannel;

    public IssuingPoint(Channel<String> assistantIssuingPointChannel, Channel<String> issuingPointCustomerChannel, Channel<Boolean> wasBookSoldChannel, Channel<Double> payingChannel) {
        this.assistantIssuingPointChannel = assistantIssuingPointChannel;
        this.issuingPointCustomerChannel = issuingPointCustomerChannel;
        this.wasBookSoldChannel = wasBookSoldChannel;
        this.payingChannel = payingChannel;
    }

    @Override
    public void run() {
        // getBook from Assistant
        String book = assistantIssuingPointChannel.get();
        say("An assistant gave us a " + quoted(book) + " book.");

        // packBook
        say("Packing the book...");

        // put to Customer
        issuingPointCustomerChannel.put(() -> book);
        // getMoney
        Double money = payingChannel.get();
        // mark as sold
        say("We've earned " + money + "$!");
        // put sold Book to Cashier
        wasBookSoldChannel.put(() -> true);

    }
}
