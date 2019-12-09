package rmi_impl;

import sun.misc.Cleaner;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class MappedChannel {
    private String sharedFileName;
    private String emptyFileName;
    private String freeFileName;

    private MappedByteBuffer sharedMemory;
    private RandomAccessFile sharedFile;
    private FileChannel sharedChannel;

    private MappedByteBuffer emptyMemory;
    private RandomAccessFile emptyFile;
    private FileChannel emptyChannel;

    private MappedByteBuffer freeMemory;
    private RandomAccessFile freeFile;
    private FileChannel freeChannel;
    private List<RandomAccessFile> filesToClose = new ArrayList<>();
    private List<FileChannel> channelsToClose = new ArrayList<>();

    private int bufferSize = 1024; // 1KB

    public MappedChannel(String sharedFileName, int bufferSize) {
        this(sharedFileName);
        // TODO: DO NOT USE
        this.bufferSize = bufferSize;
    }

    public MappedChannel(String sharedFileName) {
        this.sharedFileName = sharedFileName;
        this.emptyFileName = "empty_" + sharedFileName;
        this.freeFileName = "free_" + sharedFileName;
        try {
            this.sharedFile = new RandomAccessFile(sharedFileName, "rw");
            this.sharedChannel = sharedFile.getChannel();
            this.sharedMemory = sharedChannel.map(FileChannel.MapMode.READ_WRITE, 0, bufferSize);

            this.emptyFile = new RandomAccessFile(emptyFileName, "rw");
            this.emptyChannel = emptyFile.getChannel();
            this.emptyMemory = emptyChannel.map(FileChannel.MapMode.READ_WRITE, 0, bufferSize);

            this.freeFile = new RandomAccessFile(freeFileName, "rw");
            this.freeChannel = freeFile.getChannel();
            this.freeMemory = freeChannel.map(FileChannel.MapMode.READ_WRITE, 0, bufferSize);


            filesToClose.add(sharedFile);
            filesToClose.add(emptyFile);
            filesToClose.add(freeFile);

            channelsToClose.add(sharedChannel);
            channelsToClose.add(emptyChannel);
            channelsToClose.add(freeChannel);
            mappedCapture(emptyMemory);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void put(Supplier<String> supplier) {
        mappedCapture(freeMemory);
        byte[] sharedData = supplier.get().getBytes();
        sharedMemory.put(sharedData);
        mappedRelease(emptyMemory);
    }

    public String get() {
        mappedCapture(emptyMemory);

        mappedRelease(freeMemory);
        byte[] buffer = new byte[bufferSize];
        sharedMemory.get(buffer);
        return new String(buffer);

    }

    private void mappedCapture(MappedByteBuffer buffer) {
        while (buffer.get() == 0) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // ?
        Cleaner cleaner = ((sun.nio.ch.DirectBuffer) buffer).cleaner();
        if (cleaner != null) {
            cleaner.clean();
        }
    }

    private void mappedRelease(MappedByteBuffer buffer) {
        buffer.put((byte) 1);
        //buffer.force();
    }

    @Override
    protected void finalize() throws Throwable {
        for (FileChannel ch : channelsToClose)
            ch.close();
        for (RandomAccessFile fl : filesToClose)
            fl.close();

        super.finalize();
    }
}
