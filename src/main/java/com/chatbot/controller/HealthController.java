package com.chatbot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@RestController
@EnableScheduling
public class HealthController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String myUrl = "https://whatsapp-chatbot-zlrx.onrender.com/";

    @GetMapping("/")
    public String home() {
        return "✅ WhatsApp Chatbot is active!";
    }

    @Scheduled(cron = "0 */2 * * * *")
    public void keepAlive() {
        try {
            restTemplate.getForEntity(myUrl, String.class);
            System.out.println("Keep-alive called at: " + java.time.LocalDateTime.now());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}