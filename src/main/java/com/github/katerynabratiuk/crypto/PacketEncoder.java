package com.github.katerynabratiuk.crypto;

import com.github.katerynabratiuk.domain.Packet;
import com.github.katerynabratiuk.crypto.util.Crc16;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Base64;

public class PacketEncoder {

    private static final byte MAGIC = 0x13;
    private static long packetCounter = 1;
    private final SecretKey key;

    public PacketEncoder(SecretKey key) {
        this.key = key;
    }

    public PacketEncoder(String base64Key) {
        byte[] decodedKey = Base64.getDecoder().decode(base64Key);
        this.key = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }

    public byte[] encode(Packet packet) {
        byte[] encryptedMsgBytes = encryptMessage(packet.getMessage().getMessageInBytes());
        int messageLength = encryptedMsgBytes.length;

        ByteBuffer byteBuffer = ByteBuffer.allocate(1 + 1 + 8 + 4 + 2 + messageLength + 2).order(ByteOrder.BIG_ENDIAN);
        byteBuffer.put(MAGIC)
                .put(packet.getSourceId())
                .putLong(packetCounter)
                .putInt(messageLength);

        byte[] header = byteBuffer.array();
        short crc16_header = Crc16.compute(header, 0, 14);
        byteBuffer.putShort(crc16_header);

        byteBuffer.put(encryptedMsgBytes);
        short crc16_message = Crc16.compute(packet.getMessage().getMessageInBytes(), 0,packet.getMessage().getLength());
        byteBuffer.putShort(crc16_message);

        packetCounter++;
        return byteBuffer.array();
    }

    private byte[] encryptMessage(byte[] plainMessage) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(plainMessage);
        } catch (Exception e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }
}
