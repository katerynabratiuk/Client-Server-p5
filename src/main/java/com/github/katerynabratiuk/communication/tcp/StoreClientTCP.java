package com.github.katerynabratiuk.communication.tcp;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;

public class StoreClientTCP implements Runnable {
    private final String host;
    private final int port;
    private final BlockingQueue<byte[]> outgoingMessages;
    private volatile boolean running = true;

    public StoreClientTCP(String host, int port, BlockingQueue<byte[]> queue) {
        this.host = host;
        this.port = port;
        this.outgoingMessages = queue;
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            try (Socket socket = new Socket()) {
                socket.connect(new InetSocketAddress(host, port), 2000);
                OutputStream output = socket.getOutputStream();

                while (running && !socket.isClosed()) {
                    byte[] msg = outgoingMessages.take();
                    output.write(msg);
                    output.flush();
                }
            } catch (IOException | InterruptedException e) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
