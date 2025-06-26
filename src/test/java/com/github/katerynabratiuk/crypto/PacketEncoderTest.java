package com.github.katerynabratiuk.crypto;

import com.github.katerynabratiuk.domain.Message;
import com.github.katerynabratiuk.domain.Packet;
import com.github.katerynabratiuk.crypto.util.Crc16;
import org.junit.jupiter.api.*;

import javax.crypto.SecretKey;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class PacketEncoderTest {

    public static PacketEncoder encoder;
    public static SecretKey key;

    @BeforeAll
    static void setUp() throws NoSuchAlgorithmException {
        SetUpUtil.initialize();
        encoder = SetUpUtil.encoder;
    }

    @Test
    @DisplayName("Ensure that packet ID is generated correctly in ascending order starting from 1.")
    void shouldProperlyGeneratePacketIdAscending()
    {
        Message msg1 = new Message("Hello World!", 3, 4);
        Packet packet1 = new Packet((byte) 1, msg1);
        byte[] encoded1 = encoder.encode(packet1);

        Message msg2 = new Message("Message2", 11, 90);
        Packet packet2 = new Packet((byte) 3, msg2);
        byte[] encoded2 = encoder.encode(packet2);

        Message msg3 = new Message("Message3", 222, 23);
        Packet packet3 = new Packet((byte) 9, msg3);
        byte[] encoded3 = encoder.encode(packet3);


        long packet1Id = ByteBuffer.wrap(Arrays.copyOfRange(encoded1, 2, 10))
                .order(ByteOrder.BIG_ENDIAN).getLong();
        long packet2Id = ByteBuffer.wrap(Arrays.copyOfRange(encoded2, 2, 10))
                .order(ByteOrder.BIG_ENDIAN).getLong();
        long packet3Id = ByteBuffer.wrap(Arrays.copyOfRange(encoded3, 2, 10))
                .order(ByteOrder.BIG_ENDIAN).getLong();


        assertEquals(packet1Id + 1, packet2Id, "Packet 2 ID should be one greater than Packet 1 ID");
        assertEquals(packet2Id + 1, packet3Id, "Packet 3 ID should be one greater than Packet 2 ID");
    }

    @Test
    @DisplayName("Ensure that header information " +
            "(magic byte, source id) " +
            "are encoded correctly for various packets.")
    void shouldProperlyEncodeHeaderInfo(){
        String message1Str = "Random text!!!";
        Message msg1 = new Message(message1Str, 515121, 121);
        Packet packet1 = new Packet((byte) 55, msg1);
        byte[] encoded1 = encoder.encode(packet1);

        assertEquals(0x13, encoded1[0], "Magic byte should equal to 13h.");
        assertEquals(55, encoded1[1], "Source id should equal 55.");


        String message2Str = "Hi";
        Message msg2 = new Message(message2Str, 999, 5);
        Packet packet2 = new Packet((byte) 7, msg2);
        byte[] encoded2 = encoder.encode(packet2);

        assertEquals(0x13, encoded2[0], "Magic byte should equal to 13h.");
        assertEquals(7, encoded2[1], "Source id should equal 7.");



        String message3Str = "This is a longer test message.";
        Message msg3 = new Message(message3Str, 88888, 42);
        Packet packet3 = new Packet((byte) 127, msg3);
        byte[] encoded3 = encoder.encode(packet3);

        assertEquals(0x13, encoded3[0], "Magic byte should equal to 13h.");
        assertEquals(127, encoded3[1], "Source id should equal 127.");
    }


    @Test
    @DisplayName("Ensure that messages are ciphered (cannot be read).")
    void shouldCipherMessage() {
        String message1Str = "Random text!!!";
        Message msg1 = new Message(message1Str, 515121, 121);
        Packet packet1 = new Packet((byte) 55, msg1);
        byte[] encoded1 = encoder.encode(packet1);
        String encoded1_str = new String(encoded1, StandardCharsets.UTF_8);

        String message2Str = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
                " Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut" +
                " aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate" +
                " velit esse cillum dolore eu fugiat nulla pariatur. " +
                "Excepteur sint occaecat cupidatat non proident, " +
                "sunt in culpa qui officia deserunt mollit anim id est laborum.";
        Message msg2 = new Message(message2Str, 999, 5);
        Packet packet2 = new Packet((byte) 7, msg2);
        byte[] encoded2 = encoder.encode(packet2);
        String encoded2_str = new String(encoded2, StandardCharsets.UTF_8);

        String message3Str = "Short message.";
        Message msg3 = new Message(message3Str, 88888, 42);
        Packet packet3 = new Packet((byte) 127, msg3);
        byte[] encoded3 = encoder.encode(packet3);
        String encoded3_str = new String(encoded3, StandardCharsets.UTF_8);


        assertFalse(encoded1_str.contains(message1Str), "Bytes (converted to string) should be ciphered (not contain original message).");
        assertFalse(encoded2_str.contains(message2Str), "Bytes (converted to string) should be ciphered (not contain original message).");
        assertFalse(encoded3_str.contains(message3Str), "Bytes (converted to string) should be ciphered (not contain original message).");

    }

    @Test
    @DisplayName("Ensure that CRC16 for header is correct.")
    void crc16HeaderIsCorrect() {
        Message msg = new Message("Hello, CRC!", 12345, 77);
        Packet packet = new Packet((byte) 11, msg);
        byte[] encoded = encoder.encode(packet);
        int expectedCrc = Crc16.compute(encoded, 0, 14);
        int actualCrc = ByteBuffer.wrap(encoded, 14, 2)
                .order(ByteOrder.BIG_ENDIAN)
                .getShort();

        assertEquals(expectedCrc, actualCrc, "CRC16 checksum of the header should match the computed value.");
    }

    @Test
    @DisplayName("Ensure that CRC16 for message is correct.")
    void crc16MessageIsCorrect() {
        Message msg = new Message("Hello, this is a test message.", 2024, 42);
        Packet packet = new Packet((byte) 99, msg);
        byte[] encoded = encoder.encode(packet);
        short expectedCrc = Crc16.compute(msg.getMessageInBytes(), 0, msg.getLength());
        short actualCrc = ByteBuffer.wrap(encoded, encoded.length - 2 , 2)
                .order(ByteOrder.BIG_ENDIAN)
                .getShort();


        String message2Str = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
                " Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut" +
                " aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate" +
                " velit esse cillum dolore eu fugiat nulla pariatur. " +
                "Excepteur sint occaecat cupidatat non proident, " +
                "sunt in culpa qui officia deserunt mollit anim id est laborum.";
        Message msg2 = new Message(message2Str, 999, 5);
        Packet packet2 = new Packet((byte) 44, msg2);
        byte[] encoded2 = encoder.encode(packet2);
        short expected2Crc = Crc16.compute(msg2.getMessageInBytes(), 0, msg2.getLength());
        short actual2Crc = ByteBuffer.wrap(encoded2, encoded2.length - 2 , 2)
                .order(ByteOrder.BIG_ENDIAN)
                .getShort();


        assertEquals(expectedCrc, actualCrc, "CRC16 checksum of the message should match the computed value.");
        assertEquals(expected2Crc, actual2Crc, "CRC16 checksum of the message should match the computed value.");
    }

}