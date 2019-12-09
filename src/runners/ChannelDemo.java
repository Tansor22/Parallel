package runners;

import utils.ConcurrencyUtils;
import primitives.Channel;

import java.util.Arrays;
import java.util.Random;

public class ChannelDemo {
    public static void main(String[] args) {
        Channel<int[]> channel = new Channel<>();
        final int n = 1_000;
        Thread a = ConcurrencyUtils.createThread(() ->
                channel.put(() -> {
                    int[] data = new int[n];
                    Random r = new Random(System.currentTimeMillis());
                    for (int i = 0; i < n; i++) {
                        data[i] = r.nextInt(i + 10);
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    return data;
                })
        , "A");
        final int[][] data = new int[1][1];
        Thread b = ConcurrencyUtils.createThread(() -> data[0] = channel.get(), "B");
        b.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        a.start();
        // barrier
        ConcurrencyUtils.waitForMultipleThreads(b);

        System.out.println("Data = " + Arrays.toString(data[0]));
    }
}
