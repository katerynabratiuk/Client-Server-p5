package com.github.katerynabratiuk.domain;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class Message {

    private int CType;
    private int UserId;
    private String message;
    private final int length;

    public Message(byte[] messageByteArr) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(messageByteArr);

        CType = byteBuffer.getInt();
        UserId = byteBuffer.getInt();

        byte[] msgBytes = new byte[messageByteArr.length - 8];
        byteBuffer.get(msgBytes);

        this.message = new String(msgBytes, StandardCharsets.UTF_8);
        this.length = messageByteArr.length;
    }

    public Message(byte[] messageByteArr, int CType, int userId)
    {
        this.CType = CType;
        this.UserId = userId;
        this.message = new String(messageByteArr, StandardCharsets.UTF_8);
        this.length = messageByteArr.length + 8;
    }

    public Message(String message, int CType, int userId) {
        this.CType = CType;
        this.UserId = userId;
        this.message = message;
        this.length = message.length() + 8;
    }

    public int getCType() {
        return CType;
    }

    public void setCType(int CType) {
        this.CType = CType;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        this.UserId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public byte[] getMessageInBytes() {
        byte[] msgBytes = message.getBytes(StandardCharsets.UTF_8);
        ByteBuffer buffer = ByteBuffer.allocate(8 + msgBytes.length);
        buffer.putInt(CType);
        buffer.putInt(UserId);
        buffer.put(msgBytes);
        return buffer.array();
    }

    public int getLength() {
        return length;
    }
}
