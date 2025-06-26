package com.github.katerynabratiuk.communication.udp;

public class ServerMain {

    public static void main(String[] args) throws Exception {
        StoreServerUDP server = new StoreServerUDP(8081);
        server.listen();
    }

}
