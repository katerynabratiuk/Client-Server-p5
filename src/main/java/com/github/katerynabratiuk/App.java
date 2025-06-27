package com.github.katerynabratiuk;
import com.github.katerynabratiuk.handler.LoginHandler;
import com.github.katerynabratiuk.handler.ProductHandler;
import com.github.katerynabratiuk.repository.implementation.ProductRepositoryImpl;
import com.github.katerynabratiuk.repository.implementation.UserRepositoryImpl;
import com.github.katerynabratiuk.service.implementation.ProductServiceImpl;
import com.github.katerynabratiuk.service.implementation.UserServiceImpl;
import com.sun.net.httpserver.HttpServer;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class App {
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(InetAddress.getLoopbackAddress(), 62550), 10);

        server.createContext("/login", new LoginHandler(
                new UserServiceImpl(new UserRepositoryImpl())
        ));

        ProductRepositoryImpl productRepo = new ProductRepositoryImpl();
        ProductServiceImpl productService = new ProductServiceImpl(productRepo);
        server.createContext("/api/product", new ProductHandler(productService));

        server.start();
        
    }
}
