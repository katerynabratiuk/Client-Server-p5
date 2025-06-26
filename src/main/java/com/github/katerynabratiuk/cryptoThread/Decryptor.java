package com.github.katerynabratiuk.cryptoThread;

import com.github.katerynabratiuk.crypto.PacketDecoder;
import com.github.katerynabratiuk.domain.Message;
import com.github.katerynabratiuk.domain.Packet;

import java.util.concurrent.BlockingQueue;

public class Decryptor implements IDecryptor, Runnable {

    private final BlockingQueue<byte[]> inputQueue;
    private final BlockingQueue<Message> outputQueue;
    private final PacketDecoder decoder;
    private volatile boolean running = true;

    public Decryptor(BlockingQueue<byte[]> inputQueue,
                     BlockingQueue<Message> outputQueue,
                     PacketDecoder decoder) {
        this.inputQueue = inputQueue;
        this.outputQueue = outputQueue;
        this.decoder = decoder;
    }

    @Override
    public void run() {
        try {
            while (running) {
                byte[] encoded = inputQueue.take();
                decrypt(encoded);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Decryptor thread was interrupted.");
        }
    }

    @Override
    public void decrypt(byte[] message) {
        try {
            Packet packet = decoder.decode(message);
            outputQueue.put(packet.getMessage());
            System.out.println("Thread " + Thread.currentThread().getName() + " just decrypted a message!");
        } catch (Exception e) {
            System.err.println("Failed to decrypt message: " + e.getMessage());
        }
    }

    public void stop()
    {
        running = false;
    }
}
