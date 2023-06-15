package com.example.staratlas;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Config {
    public static int WIDTH;
    public static int HEIGHT;
    public static double CAMERA_PITCH;
    public static double CAMERA_YAW;
    public static double CAMERA_FOV;

    public static void load(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim().replace(";", "");
                    switch (key) {
                        case "WIDTH":
                            WIDTH = Integer.parseInt(value);
                            break;
                        case "HEIGHT":
                            HEIGHT = Integer.parseInt(value);
                            break;
                        case "CAMERA_PITCH":
                            CAMERA_PITCH = Double.parseDouble(value);
                            break;
                        case "CAMERA_YAW":
                            CAMERA_YAW = Double.parseDouble(value);
                            break;
                        case "CAMERA_FOV":
                            CAMERA_FOV = Double.parseDouble(value);
                            break;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading config file: " + fileName);
        }
    }
}
