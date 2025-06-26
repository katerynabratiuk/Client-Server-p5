package com.github.katerynabratiuk.communication.tcp;

import com.github.katerynabratiuk.communication.tcp.StoreClientTCP;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Client {
    public static void startClients(int count, String host, int port) {
        for (int i = 0; i < count; i++) {
            BlockingQueue<byte[]> queue = new LinkedBlockingQueue<>();
            StoreClientTCP client = new StoreClientTCP(host, port, queue);
            new Thread(client).start();

            // Simulate sending
            new Thread(() -> {
                try {
                    for (int j = 0; j < 10; j++) {
                        queue.put(("Message from client " + Thread.currentThread().getName() + " #" + j)
                                .getBytes(StandardCharsets.UTF_8));
                        Thread.sleep(500);
                    }
                } catch (InterruptedException ignored) {}
            }).start();
        }
    }
}
