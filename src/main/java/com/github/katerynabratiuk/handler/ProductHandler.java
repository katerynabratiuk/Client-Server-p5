package com.github.katerynabratiuk.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.katerynabratiuk.entity.Product;
import com.github.katerynabratiuk.service.implementation.ProductServiceImpl;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProductHandler extends AuthHandler {

    private final ProductServiceImpl productService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ProductHandler(ProductServiceImpl productService) {
        this.productService = productService;
    }

    @Override
    public void handleAuthorized(HttpExchange exchange, String login) throws IOException {
        try {
            switch (exchange.getRequestMethod()) {
                case "GET" -> handleGet(exchange);
                case "PUT" -> handleCreate(exchange);
                case "POST" -> handleUpdate(exchange);
                case "DELETE" -> handleDelete(exchange);
                default -> exchange.sendResponseHeaders(405, -1);
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            byte[] err = e.getMessage().getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(409, err.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(err);
            }
        } finally {
            exchange.close();
        }
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        Integer id = extractId(exchange);
        if (id == null) {
            exchange.sendResponseHeaders(400, -1);
            return;
        }

        Product product = productService.get(id);
        if (product == null) {
            exchange.sendResponseHeaders(404, -1);
            return;
        }

        byte[] response = objectMapper.writeValueAsBytes(product);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.length);
        exchange.getResponseBody().write(response);
    }

    private void handleCreate(HttpExchange exchange) throws IOException {
        Product product = objectMapper.readValue(exchange.getRequestBody(), Product.class);
        System.out.println("Creating product: " + product);

        if (product.getPrice().signum() < 0) {
            exchange.sendResponseHeaders(409, -1);
            return;
        }

        Product created = productService.create(product);
        if (created == null) {
            exchange.sendResponseHeaders(409, -1);
            return;
        }

        byte[] response = objectMapper.writeValueAsBytes(
                java.util.Map.of("id", created.getId())
        );
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(201, response.length);
        exchange.getResponseBody().write(response);
    }

    private void handleUpdate(HttpExchange exchange) throws IOException {
        Integer id = extractId(exchange);
        if (id == null || productService.get(id) == null) {
            exchange.sendResponseHeaders(404, -1);
            return;
        }

        Product updatedProduct = objectMapper.readValue(exchange.getRequestBody(), Product.class);
        updatedProduct.setId(id);

        try {
            productService.update(updatedProduct);
            exchange.sendResponseHeaders(204, -1);
        } catch (RuntimeException e) {
            exchange.sendResponseHeaders(409, -1);
        }
    }

    private void handleDelete(HttpExchange exchange) throws IOException {
        Integer id = extractId(exchange);
        if (id == null || productService.get(id) == null) {
            exchange.sendResponseHeaders(404, -1);
            return;
        }

        productService.delete(id);
        exchange.sendResponseHeaders(204, -1);
    }

    private Integer extractId(HttpExchange exchange) {
        String path = exchange.getRequestURI().getPath();
        Pattern pattern = Pattern.compile("/api/product/(\\d+)");
        Matcher matcher = pattern.matcher(path);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return null;
    }
}
