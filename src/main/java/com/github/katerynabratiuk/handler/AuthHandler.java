package com.github.katerynabratiuk.handler;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public abstract class AuthHandler implements HttpHandler {

    private static final String SECRET = "verysecret";
    private static final Algorithm algorithm = Algorithm.HMAC256(SECRET);

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String authHeader = exchange.getRequestHeaders().getFirst("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.sendResponseHeaders(403, -1);
            return;
        }

        String token = authHeader.substring("Bearer ".length());

        try {
            JWTVerifier verifier = JWT.require(algorithm).withIssuer("server").build();
            DecodedJWT decodedJWT = verifier.verify(token);
            String login = decodedJWT.getSubject();

            handleAuthorized(exchange, login);

        } catch (Exception e) {
            exchange.sendResponseHeaders(403, -1);
        }
    }

    public abstract void handleAuthorized(HttpExchange exchange, String login) throws IOException;
}

