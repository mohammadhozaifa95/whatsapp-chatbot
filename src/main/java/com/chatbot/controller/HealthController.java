package com.chatbot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Health", description = "Health check and keep-alive endpoints")
public class HealthController {
    
    @Operation(
        summary = "Home page",
        description = "Returns a welcome message confirming the bot is running."
    )
    @GetMapping("/")
    public String home() {
        return "✅ WhatsApp Chatbot is active!";
    }
}