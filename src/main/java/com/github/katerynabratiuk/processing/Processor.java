package com.github.katerynabratiuk.processing;

import com.github.katerynabratiuk.command.CommandMessage;
import com.github.katerynabratiuk.command.CommandType;
import com.github.katerynabratiuk.domain.Message;

import java.util.concurrent.BlockingQueue;

public class Processor implements IProcessor, Runnable{

    //private final InventoryServiceImpl inventoryService;
    private final BlockingQueue<Message> incomingMessages;
    private volatile boolean running = true;

    public Processor(/*InventoryServiceImpl inventoryService,*/ BlockingQueue<Message> incomingMessages) {
        //this.inventoryService = inventoryService;
        this.incomingMessages = incomingMessages;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Message msg = incomingMessages.take();
                try {
                    process(msg);
                } catch (Exception e) {
                    System.err.println("Failed to process message: " + e.getMessage());
                    Thread.currentThread().interrupt();
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Processor thread interrupted, shutting down.");
                break;
            }
        }
    }

    @Override
    public void process(Message message) {
        CommandMessage cm = new CommandMessage(message);
        CommandType ct = cm.getType();
        String[] args = cm.getArguments();
        int quantity = Integer.parseInt(args[1]);
        String productName = args[0];
//        synchronized (inventoryService){
//            switch (ct){
//                case INCREASE_QUANTITY:
////                    inventoryService.addBatch(productName, quantity);
////                    System.out.println(STR."Thread \{Thread.currentThread().getName()} just processed a message!");
//                    break;
//
//                case DECREASE_QUANTITY:
////                    inventoryService.removeBatch(productName, quantity);
//                    break;
//
//            }
//        }

    }

    public void stop()
    {
        running = false;
    }

}
