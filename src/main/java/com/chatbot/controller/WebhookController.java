package com.chatbot.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chatbot.exception.InvalidMessageException;
import com.chatbot.model.MessageRequest;
import com.chatbot.service.ChatbotService;
import com.chatbot.utils.MessageLogger;

@RestController
@RequestMapping("/webhook")
public class WebhookController {
    
    private final ChatbotService chatbotService;
    
    public WebhookController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }
    
    @PostMapping
    public Map<String, Object> receive(@RequestBody MessageRequest req) {
        try {
            // Validate input
            if (req == null) {
                throw new InvalidMessageException("Request body is empty");
            }
            
            if (req.getFrom() == null || req.getFrom().trim().isEmpty()) {
                throw new InvalidMessageException("Sender number is required");
            }
            
            MessageLogger.info("From: " + req.getFrom() + " | Msg: " + req.getMessage());
            
            String reply = chatbotService.getReply(req.getMessage(), req.getFrom());
            
            MessageLogger.info("Reply: " + reply);
            
            Map<String, Object> res = new HashMap<>();
            res.put("status", "ok");
            res.put("reply", reply);
            res.put("time", System.currentTimeMillis());
            return res;
            
        } catch (InvalidMessageException e) {
            throw e; // Global handler will catch this
        } catch (Exception e) {
            MessageLogger.error("Error processing message: " + e.getMessage());
            Map<String, Object> error = new HashMap<>();
            error.put("status", "error");
            error.put("message", "Failed to process message");
            return error;
        }
    }
    
    @GetMapping
    public String verify() {
        return "Bot is running";
    }
}