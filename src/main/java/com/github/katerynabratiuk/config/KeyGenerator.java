package com.github.katerynabratiuk.config;

import java.security.SecureRandom;
import java.util.Base64;

public class KeyGenerator {
    public static void main(String[] args) {
        byte[] key = new byte[32]; // 256-bit
        new SecureRandom().nextBytes(key);
        String base64Key = Base64.getEncoder().encodeToString(key);
        System.out.println("Generated Key (for config.properties):");
        System.out.println("secret.key=" + base64Key);
    }
}
