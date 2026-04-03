package com.chatbot.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "WhatsApp message request payload")
public class MessageRequest {
    
    @Schema(description = "Sender's phone number", example = "+919876543210", required = true)
    private String from;
    
    @Schema(description = "Message text content", example = "hi", required = true)
    private String message;
    
    @Schema(hidden = true)  // Hide messageType
    private String messageType;
    
    @Schema(description = "Unix timestamp of the message", example = "1712000000000")
    private long timestamp;
}