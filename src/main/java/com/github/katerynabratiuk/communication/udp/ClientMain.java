package com.github.katerynabratiuk.communication.udp;

import java.nio.charset.StandardCharsets;

public class ClientMain {

    public static void main(String[] args) throws Exception {
        StoreClientUDP client = new StoreClientUDP("localhost", 8081);
        client.send("INCREASE_QUANTITY/chocolate bar/10".getBytes(StandardCharsets.UTF_8));
        client.close();
    }

}
