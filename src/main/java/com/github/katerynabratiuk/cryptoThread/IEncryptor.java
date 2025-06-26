package com.github.katerynabratiuk.cryptoThread;

import com.github.katerynabratiuk.domain.Message;
import com.github.katerynabratiuk.domain.Packet;

public interface IEncryptor {
    byte[] encrypt(Message message);
}
