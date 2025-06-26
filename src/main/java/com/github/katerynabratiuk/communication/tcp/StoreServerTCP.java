// TCP server with multithreading
package com.github.katerynabratiuk.communication.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class StoreServerTCP {
    private final ServerSocket serverSocket;

    public StoreServerTCP(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
    }

    public void start() {
        new Thread(() -> {
            try {
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    new Thread(() -> handleClient(clientSocket)).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void handleClient(Socket socket) {
        try (InputStream in = socket.getInputStream()) {
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                String msg = new String(buffer, 0, read);
                System.out.println("[TCP Server] Received: " + msg);
            }
        } catch (IOException e) {
            System.out.println("[TCP Server] Client disconnected.");
        }
    }
}