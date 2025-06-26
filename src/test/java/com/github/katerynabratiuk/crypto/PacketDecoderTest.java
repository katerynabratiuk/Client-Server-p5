package com.github.katerynabratiuk.crypto;

import com.github.katerynabratiuk.domain.Message;
import com.github.katerynabratiuk.domain.Packet;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

class PacketDecoderTest {

    private static PacketEncoder encoder;
    private static PacketDecoder decoder;

    @BeforeAll
    static void setUp() throws NoSuchAlgorithmException {
        SetUpUtil.initialize();
        decoder = SetUpUtil.decoder;
        encoder = SetUpUtil.encoder;
    }


    @Test
    @DisplayName("Ensure that the exception is thrown in case of incorrect magic byte")
    void incorrectMagicByte_shouldThrow()
    {
        String incorrectMagicByte = "00 01 00 00 00 00 00 00 00 " +
                "01 00 00 00 20 4A 2C 4D 9D 2F 26 DE 81 BA 84 4D " +
                "5C 65 07 33 1B 35 25 83 C5 92 4C 89 27 0A 8B 2E" +
                " EE 8E AC DD FE B8 BB 23 40";
        incorrectMagicByte = incorrectMagicByte.replace(" ", "");
        String finalIncorrectMagicByte = incorrectMagicByte;
        assertThrows(IllegalArgumentException.class, () -> decoder.decode(finalIncorrectMagicByte.getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    @DisplayName("Ensure that the exception is thrown in case of incorrect CRC16 for header")
    void incorrectCrc16Header_shouldThrow()
    {
        String incorrectHeaderCrc16 = "13 01 00 00 00 00 00 00 00 " +
                "01 00 00 00 23 99 2C 4D 9D 2F 26 DE 81 BA 84 4D " +
                "5C 65 07 33 1B 35 25 83 C5 92 4C 89 27 0A 8B 2E" +
                " EE 8E AC DD FE B8 BB 23 40";
        incorrectHeaderCrc16 = incorrectHeaderCrc16.replace(" ", "");
        String finalIncorrectHeaderCrc16 = incorrectHeaderCrc16;
        assertThrows(IllegalArgumentException.class, () -> decoder.decode(finalIncorrectHeaderCrc16.getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    @DisplayName("Ensure that the exception is thrown in case of incorrect CRC16 for message")
    void incorrectCrc16Message_shouldThrow()
    {
        String incorrectMessageCrc16 = "13 01 00 00 00 00 00 00 00 " +
                "01 00 00 00 20 4A 2C 4D 9D 2F 26 DE 81 BA 84 4D " +
                "5C 65 07 33 1B 35 25 83 C5 92 4C 89 27 0A 8B 2E" +
                " EE 8E AC DD FE B8 BB 19 00";
        incorrectMessageCrc16 = incorrectMessageCrc16.replace(" ", "");
        String finalIncorrectMessageCrc16 = incorrectMessageCrc16;
        assertThrows(IllegalArgumentException.class, () -> decoder.decode(finalIncorrectMessageCrc16.getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    @DisplayName("Ensure encoded message is correctly decoded.")
    void encodeAndDecodeMessage()
    {
        String initialMessage1 = "Hello, World!";
        int initialCType1 = 9;
        int initialUserId1 = 12;
        byte[] encoded1 = encoder.encode(new Packet((byte)3, new Message(initialMessage1, 9, 12)));

        Packet decodedPacket = decoder.decode(encoded1);
        assertEquals(initialMessage1, decodedPacket.getMessage().getMessage());
        assertEquals(initialCType1, decodedPacket.getMessage().getCType());
        assertEquals(initialUserId1, decodedPacket.getMessage().getUserId());


        String initialMessage2 = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
                " Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut" +
                " aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate" +
                " velit esse cillum dolore eu fugiat nulla pariatur. " +
                "Excepteur sint occaecat cupidatat non proident, " +
                "sunt in culpa qui officia deserunt mollit anim id est laborum.";
        int initialCType2 = 92;
        int initialUserId2 = 111;
        byte[] encoded2 = encoder.encode(new Packet((byte)3, new Message(initialMessage2, initialCType2, initialUserId2)));

        Packet decodedPacket2 = decoder.decode(encoded2);
        assertEquals(initialMessage2, decodedPacket2.getMessage().getMessage());
        assertEquals(initialCType2, decodedPacket2.getMessage().getCType());
        assertEquals(initialUserId2, decodedPacket2.getMessage().getUserId());
    }

}