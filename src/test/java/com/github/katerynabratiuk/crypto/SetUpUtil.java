package com.github.katerynabratiuk.crypto;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

public class SetUpUtil {
    public static PacketDecoder decoder;
    public static PacketEncoder encoder;
    public static SecretKey key;

    public static void initialize() throws NoSuchAlgorithmException {
        KeyGenerator keygen = KeyGenerator.getInstance("AES");
        keygen.init(256);
        key = keygen.generateKey();
        encoder = new PacketEncoder(key);
        decoder = new PacketDecoder(key);
    }
}
