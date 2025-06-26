package com.github.katerynabratiuk.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    private static final String SECRET_KEY;

    static {
        Properties props = new Properties();
        try (InputStream input = Config.class.getClassLoader().getResourceAsStream("config.properties")) {
            props.load(input);
            SECRET_KEY = props.getProperty("secret.key");
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config", e);
        }
    }

    public static String getSecretKey() {
        return SECRET_KEY;
    }
}
