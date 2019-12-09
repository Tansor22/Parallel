package patterns;

import utils.ConcurrencyUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class ConsumersAndProducersRunner {
    private static final int CONSUMERS = 5;
    private static final int PRODUCERS = 10;
    private static final int MAX_NUM = 50;
    private static final int BUFFER_SIZE = 100;
    public static void main(String[] args) {
        BoundedBuffer<Integer> bb = new BoundedBuffer<>(BUFFER_SIZE);
        List<Thread> threads = new ArrayList<>(CONSUMERS + PRODUCERS);

        // producers
        IntStream
                .range(0, PRODUCERS)
                .mapToObj(i -> ConcurrencyUtils.createInfiniteThread(() -> {
                            int valueToAdd = Utils.getRandomInt(MAX_NUM);
                            bb.put(valueToAdd);
                            ConcurrencyUtils.say("I've put the " + valueToAdd + " value.");
                        }
                        , "Producer " + (i + 1))).forEach(threads::add);
        //consumers
        IntStream
                .range(0, CONSUMERS)
                .mapToObj(i -> ConcurrencyUtils.createInfiniteThread(() -> {
                            int valueGot =  bb.take();
                            ConcurrencyUtils.say("I've taken the " + valueGot + " value.", 1);
                        }
                        , "Consumer " + (i + 1))).forEach(threads::add);

        //start
        ConcurrencyUtils.startMultipleThreadsMessy(threads);

        // CONCLUSION
        // Cases:
        // CONSUMERS > PRODUCERS => got stuck in a take method, waiting for a producer to fill
        // CONSUMERS < PRODUCERS => no stacking, consumers synchronized get values taken, the rest of put operations ignored by consumers
        // CONSUMERS = PRODUCERS => no stacking, synchronized take and put operations
    }
}
