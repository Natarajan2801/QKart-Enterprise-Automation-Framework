package com.qkart.utils;

import com.qkart.constants.FrameworkConstants;
import java.io.FileInputStream;
import java.util.Properties;

public class ConfigLoader {
    private static Properties properties;

    public static void initialize() {
        properties = new Properties();
        try {
            FileInputStream fis = new FileInputStream(FrameworkConstants.CONFIG_FILE_PATH);
            properties.load(fis);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config.properties. Check path: " + FrameworkConstants.CONFIG_FILE_PATH);
        }
    }

    public static String get(String key) {
        if (properties == null) initialize();
        return properties.getProperty(key);
    }
}