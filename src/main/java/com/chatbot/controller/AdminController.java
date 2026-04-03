package com.chatbot.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chatbot.exception.InvalidMessageException;
import com.chatbot.exception.RuleNotFoundException;
import com.chatbot.model.RuleRequest;
import com.chatbot.service.ChatbotService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin", description = "Dynamic rule management endpoints")
public class AdminController {

    private final ChatbotService chatbotService;

    public AdminController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    @Operation(
        summary = "Add a new reply rule",
        description = "Adds a new keyword-response pair dynamically. The rule works immediately without restarting the server."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rule added successfully",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(value = "{\"success\":true,\"message\":\"Rule added\",\"keyword\":\"love\",\"response\":\"I love you too! ❤️\"}"))),
        @ApiResponse(responseCode = "400", description = "Invalid input",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(value = "{\"success\":false,\"message\":\"Failed to add\"}")))
    })
    @PostMapping("/add")
    public Map<String, Object> addRule(@RequestBody RuleRequest req) {

        String keyword = req.getKeyword();
        String response = req.getResponse();

        if (keyword == null || keyword.trim().isEmpty()) {
            throw new InvalidMessageException("Keyword cannot be empty");
        }

        if (response == null || response.trim().isEmpty()) {
            throw new InvalidMessageException("Response cannot be empty");
        }

        boolean success = chatbotService.addRule(keyword, response);

        Map<String, Object> res = new HashMap<>();
        res.put("success", success);
        res.put("message", success ? "Rule added" : "Failed to add");

        if (success) {
            res.put("keyword", keyword);
            res.put("response", response);
        }

        return res;
    }

    @Operation(
        summary = "Delete a reply rule",
        description = "Deletes an existing dynamic rule. Built-in rules (hi, hello, bye, etc.) cannot be deleted."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rule deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Rule not found or is default rule")
    })
    @DeleteMapping("/delete/{keyword}")
    public Map<String, Object> deleteRule(
            @Parameter(description = "Keyword to delete", example = "love")
            @PathVariable String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new InvalidMessageException("Keyword cannot be empty");
        }

        boolean success = chatbotService.removeRule(keyword);

        if (!success) {
            throw new RuleNotFoundException(keyword);
        }

        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("message", "Rule deleted: " + keyword);
        return res;
    }

    @Operation(
        summary = "Get all reply rules",
        description = "Returns all rules including built-in and dynamic rules with their details."
    )
    @GetMapping("/rules")
    public Map<String, Object> getRules() {
        try {
            return chatbotService.getAllRules();
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", "error");
            error.put("message", "Failed to fetch rules");
            return error;
        }
    }
}