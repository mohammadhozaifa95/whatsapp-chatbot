package com.chatbot.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MessageLogger {
    
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public static void log(String level, String message) {
        String timestamp = LocalDateTime.now().format(formatter);
        System.out.println("[" + timestamp + "] [" + level + "] " + message);
    }
    
    public static void info(String message) {
        log("INFO", message);
    }
    
    public static void error(String message) {
        log("ERROR", message);
    }
    
    public static void warn(String message) {
        log("WARN", message);
    }
    
    public static void debug(String message) {
        log("DEBUG", message);
    }
}