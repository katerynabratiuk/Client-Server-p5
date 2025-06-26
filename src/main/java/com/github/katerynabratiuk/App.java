package com.github.katerynabratiuk;

import com.github.katerynabratiuk.entity.Category;
import com.github.katerynabratiuk.entity.Product;
import com.github.katerynabratiuk.repository.DbConnection;
import com.github.katerynabratiuk.repository.criteria.ProductSearchCriteria;
import com.github.katerynabratiuk.repository.implementation.CategoryRepositoryImpl;
import com.github.katerynabratiuk.repository.implementation.ProductRepositoryImpl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;

public class App
{


    public static void main(String[] args) throws Exception {
//        BlockingQueue<byte[]> encryptedQueue = new LinkedBlockingQueue<>();
//        BlockingQueue<Message> messageQueue = new LinkedBlockingQueue<>();
//
//        // Receiver
//        Receiver receiver = new Receiver(encryptedQueue);
//
//        // Decoder
//        SecretKey key = receiver.getKEY(); // потрібно створити getKEY()
//        PacketDecoder decoder = new PacketDecoder(key);
//        Decryptor decryptor = new Decryptor(encryptedQueue, messageQueue, decoder);
//
//        // Processor
//        ProductRepositoryImpl inventoryRepository = new ProductRepositoryImpl();
//        InventoryServiceImpl inventoryService = new InventoryServiceImpl(inventoryRepository); // твій сервіс
//        Processor processor = new Processor(inventoryService, messageQueue);
//        inventoryRepository.addNewProduct(new Product("chocolate bar", 7, BigDecimal.TEN));
//
//        Thread receiverThread = new Thread(receiver);
//        receiverThread.setName("Receiver thread");
//
//        Thread decryptorThread = new Thread(decryptor);
//        decryptorThread.setName("Decryptor thread");
//
//        Thread processorThread = new Thread(processor);
//        processorThread.setName("Processor thread");
//
//        receiverThread.start();
//        decryptorThread.start();
//        processorThread.start();
//
//        int port = 8888;
//
//        new Thread(() -> {
//            StoreServerTCP server = null;
//            try {
//                server = new StoreServerTCP(port);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//            server.start();
//        }).start();
//
//        try {
//            Thread.sleep(500);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        Client.startClients(3, "localhost", 5);

        CategoryRepositoryImpl categoryRepository = new CategoryRepositoryImpl();
        String[] categories = {"Diary", "Snacks", "Soft drinks"};

//        try(Connection conn = DbConnection.getConnection())
//        {
//            for(String category : categories)
//            {
//                categoryRepository.create(new Category(category));
//            }
//
//        }
//
//        List<Category> categoriesDB = categoryRepository.getAll();
//        System.out.println(categoriesDB);



        try (Connection conn = DbConnection.getConnection()) {
//            System.out.println("Successfully connected to the database.");
//            Product product = new Product(3, "Milk", 34, new BigDecimal(44), new Category(1));
              ProductRepositoryImpl productRepository = new ProductRepositoryImpl();
//            productRepository.create(product);
//            System.out.println(productRepository.getAll());
            ProductSearchCriteria criteria = new ProductSearchCriteria();
            //criteria.setLowerLimit(new BigDecimal(10));
            criteria.setName("Mi");
            System.out.println(productRepository.findByCriteria(criteria));

        } catch (Exception e) {
            System.err.println("Failed to connect: " + e.getMessage());
        }

    }
}
