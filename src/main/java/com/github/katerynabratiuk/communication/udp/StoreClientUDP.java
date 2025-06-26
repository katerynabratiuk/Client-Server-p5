package com.github.katerynabratiuk.communication.udp;

import com.github.katerynabratiuk.domain.Packet;

import java.io.*;
import java.net.*;

public class StoreClientUDP implements Closeable{

    private final DatagramSocket socket;
    private final InetAddress serverAddress;
    private final int serverPort;

    private final int MAX_ATTEMPTS = 5;

    public StoreClientUDP(String host, int port) throws IOException {
        this.socket = new DatagramSocket();
        this.serverAddress = InetAddress.getByName(host);
        this.serverPort = port;
        this.socket.setSoTimeout(1000);
    }

    public void send(byte[] msg) throws IOException {
        DatagramPacket packet = new DatagramPacket(msg, msg.length, serverAddress, serverPort);
        byte[] ackBuffer = new byte[16];
        DatagramPacket ackPacket = new DatagramPacket(ackBuffer, ackBuffer.length);

        for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
            socket.send(packet);
            try {
                socket.receive(ackPacket);
                break;
            } catch (SocketTimeoutException e) {
                // next attempt to send
            }
        }

//        String response = new String(ackPacket.getData(), 0 , ackPacket.getLength());
//
//        if(!response.equals("ACK"))
//        {
//            socket.send(packet);
//        }
    }

    @Override
    public void close() {
        socket.close();
    }
}

