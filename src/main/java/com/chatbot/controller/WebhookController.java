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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/webhook")
@Tag(name = "Webhook", description = "WhatsApp message receiving endpoints")
public class WebhookController {
    
    private final ChatbotService chatbotService;
    
    public WebhookController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }
    
    @Operation(
        summary = "Receive WhatsApp message",
        description = "Accepts JSON input simulating WhatsApp messages and returns predefined replies. Supports 'hi' → 'Hello', 'bye' → 'Goodbye', and dynamic rules added via admin API."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Message processed successfully",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(value = "{\"status\":\"ok\",\"reply\":\"Hello! 👋\",\"time\":1712000000123}"))),
        @ApiResponse(responseCode = "400", description = "Invalid request body",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(value = "{\"status\":\"error\",\"message\":\"Sender number is required\"}")))
    })
    @PostMapping
    public Map<String, Object> receive(
            @Parameter(description = "WhatsApp message payload", required = true)
            @RequestBody MessageRequest req) {
        try {
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
            throw e;
        } catch (Exception e) {
            MessageLogger.error("Error processing message: " + e.getMessage());
            Map<String, Object> error = new HashMap<>();
            error.put("status", "error");
            error.put("message", "Failed to process message");
            return error;
        }
    }
    
    @Operation(
        summary = "Webhook verification",
        description = "GET endpoint for webhook verification (required by Meta/WhatsApp API)"
    )
    @GetMapping
    public String verify() {
        return "Bot is running";
    }
}