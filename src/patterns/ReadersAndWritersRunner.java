package patterns;

import utils.ConcurrencyUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ReadersAndWritersRunner {
    private static final int READERS = 10;
    private static final int WRITERS = 5;
    private static final int MAX_NUM = 50;

    public static void main(String[] args) {
        ReadWriteList<Integer> rwl = new ReadWriteList<>();
        // adding MAX_NUM numbers
        IntStream
                .generate((() -> Utils.getRandomInt(MAX_NUM)))
                .limit(100).forEach(rwl::add);

        // writers
        List<Thread> writers = IntStream
                .range(0, WRITERS)
                .mapToObj(i -> ConcurrencyUtils.createThread(() -> {
                            int valueToAdd = Utils.getRandomInt(MAX_NUM);
                            rwl.add(valueToAdd);
                            ConcurrencyUtils.say("I've added the " + valueToAdd + " value.");
                        }
                        , "Writer " + (i + 1)))
                .collect(Collectors.toList());

        // readers
        List<Thread> readers = IntStream
                .range(0, READERS)
                .mapToObj(i -> ConcurrencyUtils.createThread(() -> {
                    // BECAUSE OF IT
                    //int indexToGet = Utils.getRandomInt(rwl.size());
                    //int valueGot = rwl.get(indexToGet);
                    int valueGot = rwl.getRandom();
                    //ConcurrencyUtils.say("I've got the " + valueGot + " value with index " + indexToGet + ".", 1);
                    ConcurrencyUtils.say("I've got the " + valueGot + " value.", 1);
                }, "Reader " + (i + 1)))
                .collect(Collectors.toList());

        ConcurrencyUtils.startMultipleThreadsMessy(writers, readers);

        // CONCLUSION
        // If list is not populated initially IndexOutOfBoundsException can be thrown
        //Multiple threads can read the data at the same time, as long as there’s no thread is updating the data.
        //Only one thread can update the data at a time, causing utils threads (both readers and writers) block until the write lock is released.
        //If a thread attempts to update the data while utils threads are reading, the write thread also blocks until the read lock is released.

    }

}

