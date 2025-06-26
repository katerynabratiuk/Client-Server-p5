package com.github.katerynabratiuk.communication.tcp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private final Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try (
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));
                OutputStream out = clientSocket.getOutputStream()
        ) {
            String input;
            while ((input = in.readLine()) != null) {
                System.out.println("Received from client: " + input);
                out.write("ACK\n".getBytes());
                out.flush();
            }
        } catch (Exception e) {
            System.err.println("Client disconnected: " + clientSocket.getRemoteSocketAddress());
        }
    }
}
