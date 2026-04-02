package com.chatbot.exception;

public class InvalidMessageException extends BotException {
    
    public InvalidMessageException(String message) {
        super("Invalid message: " + message);
    }
}