package com.github.katerynabratiuk.network;

import java.net.InetAddress;

public interface ISender {
    void sendMessage(byte[] message, InetAddress address);
}
