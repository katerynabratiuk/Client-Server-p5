package com.github.katerynabratiuk.cryptoThread;

import com.github.katerynabratiuk.crypto.PacketEncoder;
import com.github.katerynabratiuk.domain.Message;
import com.github.katerynabratiuk.domain.Packet;

import java.util.concurrent.BlockingQueue;

public class Encryptor implements IEncryptor, Runnable {

    private final BlockingQueue<Message> inputQueue;
    private final BlockingQueue<byte[]> outputQueue;
    private final PacketEncoder encoder;
    private volatile boolean running = true;

    public Encryptor(BlockingQueue<Message> inputQueue,
                     BlockingQueue<byte[]> outputQueue,
                     PacketEncoder encoder) {
        this.inputQueue = inputQueue;
        this.outputQueue = outputQueue;
        this.encoder = encoder;
    }

    @Override
    public void run() {
        try {
            while (running) {
                Message message = inputQueue.take();
                encrypt(message);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public byte[] encrypt(Message message) {
        try {
            byte[] encoded = encoder.encode(new Packet((byte)10, message));
            outputQueue.put(encoded);
            System.out.println(STR."Thread \{Thread.currentThread().getName()} just encrypted a message!");
            return encoded;
        } catch (Exception e) {
            System.err.println("Failed to encrypt message: " + e.getMessage());
            return null;
        }
    }


    public void stop()
    {
        running = false;
    }

}
