package com.github.katerynabratiuk.crypto;

import com.github.katerynabratiuk.domain.Message;
import com.github.katerynabratiuk.domain.Packet;
import com.github.katerynabratiuk.crypto.util.Crc16;

import javax.crypto.Cipher;
import java.nio.ByteBuffer;
import java.security.Key;
import java.util.Arrays;

public class PacketDecoder {

    private static final byte MAGIC = 0x13;
    private final Key key;
    public PacketDecoder(Key key) {
        this.key = key;
    }

    public Packet decode(byte[] packet)
    {
        ByteBuffer byteBuffer = ByteBuffer.wrap(packet);

        byte MAGIC = 0x13;
        if (byteBuffer.get() != MAGIC)
            throw new IllegalArgumentException("Invalid magic byte");
        byte sourceId = byteBuffer.get();
        long packetId = byteBuffer.getLong();
        int messageLength = byteBuffer.getInt();
        short Crc16_header = byteBuffer.getShort();
        if (Crc16.compute(packet, 0, 14) != Crc16_header)
            throw new IllegalArgumentException("CRC16 test on header failed");

        byte[] encryptedMsgBytes = Arrays.copyOfRange(packet, 16, 16 + messageLength);
        byte[] decryptedMsgBytes = decryptMessage(encryptedMsgBytes);
        Message message = new Message(decryptedMsgBytes);

        short Crc16_message = byteBuffer.getShort(packet.length - 2);
        if (Crc16.compute(message.getMessageInBytes(), 0, message.getLength()) != Crc16_message)
            throw new IllegalArgumentException("CRC16 test on message failed");
        return new Packet(MAGIC, sourceId, packetId, messageLength,Crc16_header, message, Crc16_message);
    }

    private byte[] decryptMessage(byte[] encryptedMessage) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(encryptedMessage);
        } catch (Exception e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }

}
