package com.github.katerynabratiuk.config;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DbConfig {
    private static final String FILE_NAME = "src/main/java/db.properties";
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = new FileInputStream(FILE_NAME)) {
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load DB config from " + FILE_NAME, e);
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }
}