package com.github.katerynabratiuk.network;

import com.github.katerynabratiuk.crypto.PacketEncoder;
import com.github.katerynabratiuk.domain.Message;
import com.github.katerynabratiuk.domain.Packet;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.BlockingQueue;
import java.util.random.RandomGenerator;

public class Receiver implements IReceiver, Runnable {

    private static final int SENDER_ID = 6;
    private static final int RECEIVER_ID = 7;
    private static final byte PACKET_TYPE = 4;

    private final BlockingQueue<byte[]> queue;
    private final SecretKey key;
    private final PacketEncoder encoder;
    private final RandomGenerator rng = RandomGenerator.getDefault();
    private volatile boolean running = true;

    public Receiver(BlockingQueue<byte[]> queue) throws NoSuchAlgorithmException {
        this.queue = queue;
        KeyGenerator keygen = KeyGenerator.getInstance("AES");
        keygen.init(256);
        this.key = keygen.generateKey();
        this.encoder = new PacketEncoder(key); // created once
    }

    @Override
    public void run() {
        try {
            while (running) {
                receiveMessage();
                Thread.sleep(1000); // simulate complex logic
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private byte[] generateMessage() {
        int quantity = rng.nextInt(1, 100);

        String[] commands = {"INCREASE_QUANTITY", "DECREASE_QUANTITY"};
        String command = commands[rng.nextInt(commands.length)];
        String payload = STR."\{command}/chocolate bar/\{quantity}";
        System.out.println(command + " " + Integer.toString(quantity));
        return payload.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public void receiveMessage() {
        try {
            Message message = new Message(generateMessage(), SENDER_ID, RECEIVER_ID);
            Packet packet = new Packet(PACKET_TYPE, message);
            byte[] encoded = encoder.encode(packet);
            queue.put(encoded);
            System.out.println("Thread " + Thread.currentThread().getName() + " just received a message!");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Receiver interrupted while putting message.");
        } catch (Exception e) {
            System.err.println("Error while encoding message: " + e.getMessage());
        }
    }

    public SecretKey getKEY() {
        return key;
    }

    public void stop()
    {
        running = false;
    }
}
