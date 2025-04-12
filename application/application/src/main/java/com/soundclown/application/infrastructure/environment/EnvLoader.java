package com.soundclown.application.infrastructure.environment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class EnvLoader {
    public static void load() {
        String envFile = ".env";
        try (BufferedReader br = new BufferedReader(new FileReader(envFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#")) {
                    continue;
                }
                String[] keyValue = line.split("=", 2);
                if (keyValue.length == 2) {
                    System.setProperty(keyValue[0].trim(), keyValue[1].trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}