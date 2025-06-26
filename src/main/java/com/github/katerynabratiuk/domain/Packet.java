package com.github.katerynabratiuk.domain;

public class Packet {

    private final byte magicByte;
    private final byte sourceId;
    private long packetId;
    private final int messageLength;
    private short Crc16_header;
    private final Message message;
    private short Crc16_message;

    public Packet(byte magicByte,
                  byte sourceId,
                  long packetId,
                  int messageLength,
                  short crc16_header,
                  Message message,
                  short crc16_message) {
        this.magicByte = magicByte;
        this.sourceId = sourceId;
        this.packetId = packetId;
        this.messageLength = messageLength;
        Crc16_header = crc16_header;
        this.message = message;
        Crc16_message = crc16_message;
    }
    public Packet(byte sourceId,
                  Message message) {
        this.magicByte = 0x13;
        this.sourceId = sourceId;
        this.messageLength = message.getLength();
        this.message = message;
    }

    public byte getMagicByte() {
        return magicByte;
    }

    public byte getSourceId() {
        return sourceId;
    }

    public long getPacketId() {
        return packetId;
    }

    public int getMessageLength() {
        return messageLength;
    }

    public short getCrc16_header() {
        return Crc16_header;
    }

    public Message getMessage() {
        return message;
    }

    public short getCrc16_message() {
        return Crc16_message;
    }

    @Override
    public String toString() {
        return "Packet{" +
                "magicByte=" + magicByte +
                ", sourceId=" + sourceId +
                ", packetId=" + packetId +
                ", messageLength=" + messageLength +
                ", Crc16_header=" + Crc16_header +
                ", message=" + message +
                ", Crc16_message=" + Crc16_message +
                '}';
    }
}
