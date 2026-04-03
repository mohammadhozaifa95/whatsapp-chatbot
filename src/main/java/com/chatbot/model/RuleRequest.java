package com.chatbot.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RuleRequest {

    @Schema(description = "Keyword to trigger response", example = "love", required = true)
    private String keyword;

    @Schema(description = "Bot response", example = "I love you too! ❤️", required = true)
    private String response;
}