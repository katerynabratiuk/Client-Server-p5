package com.github.katerynabratiuk.network;

import com.github.katerynabratiuk.domain.Message;

import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Sender implements Runnable, ISender {

    private volatile boolean running = true;
    private final BlockingQueue<byte[]> inputQueue;
    private final InetAddress target;


    public Sender(BlockingQueue<byte[]> bq, InetAddress inetAddress)
    {
        inputQueue = bq;
        target = inetAddress;
    }

    public void sendMessage(byte[] message, InetAddress target) {
        System.out.println("Message: " + new String(message, StandardCharsets.UTF_8));
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            try {
                byte[] message = inputQueue.take();
                sendMessage(message, target);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

}
