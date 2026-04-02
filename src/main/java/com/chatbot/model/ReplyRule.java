package com.chatbot.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReplyRule {
    private String keyword;      
    private String response;   
    private boolean caseSensitive;
    private long createdAt;
}