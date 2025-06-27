package com.github.katerynabratiuk.handler;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.katerynabratiuk.entity.User;
import com.github.katerynabratiuk.service.implementation.UserServiceImpl;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class LoginHandler implements HttpHandler {

    private final UserServiceImpl userService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public LoginHandler(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        try {
            User inputUser = objectMapper.readValue(exchange.getRequestBody(), User.class);
            User dbUser = userService.get(inputUser.getLogin());

            if (dbUser == null || !dbUser.getPassword().equals(inputUser.getPassword())) {
                exchange.sendResponseHeaders(401, -1);
                return;
            }

            Algorithm algorithm = Algorithm.HMAC256("verysecret");
            String token = JWT.create()
                    .withIssuer("server")
                    .withSubject(dbUser.getLogin())
                    .withClaim("Role", "Admin")
                    .sign(algorithm);

            String responseJson = objectMapper.writeValueAsString(
                    java.util.Map.of("token", token)
            );

            byte[] responseBytes = responseJson.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, responseBytes.length);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(responseBytes);
            }

        } catch (IOException e) {
            exchange.sendResponseHeaders(400, -1);
        }
    }
}
