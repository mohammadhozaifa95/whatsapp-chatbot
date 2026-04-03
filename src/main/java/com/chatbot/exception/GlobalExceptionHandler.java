package com.chatbot.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.chatbot.utils.MessageLogger;

import io.swagger.v3.oas.annotations.Hidden;

@RestControllerAdvice
@Hidden
public class GlobalExceptionHandler {
    
    @ExceptionHandler(InvalidMessageException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleInvalidMessage(InvalidMessageException ex) {
        MessageLogger.warn("Invalid message: " + ex.getMessage());
        
        Map<String, Object> error = new HashMap<>();
        error.put("status", "error");
        error.put("message", ex.getMessage());
        error.put("code", "INVALID_MESSAGE");
        return error;
    }
    
    @ExceptionHandler(RuleNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handleRuleNotFound(RuleNotFoundException ex) {
        MessageLogger.warn("Rule not found: " + ex.getMessage());
        
        Map<String, Object> error = new HashMap<>();
        error.put("status", "error");
        error.put("message", ex.getMessage());
        error.put("code", "RULE_NOT_FOUND");
        return error;
    }
    
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handleGenericException(Exception ex) {
        MessageLogger.error("Unexpected error: " + ex.getMessage());
        
        Map<String, Object> error = new HashMap<>();
        error.put("status", "error");
        error.put("message", "Something went wrong");
        error.put("code", "INTERNAL_ERROR");
        return error;
    }
}