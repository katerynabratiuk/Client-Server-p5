package com.github.katerynabratiuk.communication.udp;

import java.io.Closeable;
import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class StoreServerUDP implements Closeable {

    private final DatagramSocket socket;
    Set<String> processedIds = new HashSet<>();

    public StoreServerUDP(int port) throws SocketException {
        this.socket = new DatagramSocket(port);
    }

    public void listen() {
        byte[] buffer = new byte[1024];
        try{
            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                byte[] data = packet.getData();
                String id = new String(Arrays.copyOfRange(data, 2, 10));
                if (!processedIds.contains(id))
                {
                    // process packet logic

                    String msg = new String(packet.getData(), 0, packet.getLength());
                    System.out.println("Received: " + msg);
                    byte[] ack = "ACK".getBytes();
                    DatagramPacket ackPacket = new DatagramPacket(
                            ack, ack.length, packet.getAddress(), packet.getPort());
                    socket.send(ackPacket);
                    processedIds.add(id);
                }
                else{
                    System.out.println("already received");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
    }
    }

    @Override
    public void close() {
        socket.close();
    }

}
