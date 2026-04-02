package com.chatbot.exception;

public class RuleNotFoundException extends BotException {
    
    public RuleNotFoundException(String keyword) {
        super("Rule not found: " + keyword);
    }
}