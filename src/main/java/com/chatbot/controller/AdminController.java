package com.chatbot.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.*;

import com.chatbot.exception.InvalidMessageException;
import com.chatbot.exception.RuleNotFoundException;
import com.chatbot.service.ChatbotService;

@RestController
@RequestMapping("/admin")
public class AdminController {

	private final ChatbotService chatbotService;

	public AdminController(ChatbotService chatbotService) {
		this.chatbotService = chatbotService;
	}

	@PostMapping("/add")
	public Map<String, Object> addRule(@RequestBody Map<String, String> req) {
		String keyword = req.get("keyword");
		String response = req.get("response");

		// Validation
		if (keyword == null || keyword.trim().isEmpty()) {
			throw new InvalidMessageException("Keyword cannot be empty");
		}

		if (response == null || response.trim().isEmpty()) {
			throw new InvalidMessageException("Response cannot be empty");
		}

		boolean success = chatbotService.addRule(keyword, response);

		Map<String, Object> res = new HashMap<>();
		res.put("success", success);
		res.put("message", success ? "Rule added" : "Failed to add (maybe duplicate or invalid)");

		if (success) {
			res.put("keyword", keyword);
			res.put("response", response);
		}

		return res;
	}

	@DeleteMapping("/delete/{keyword}")
	public Map<String, Object> deleteRule(@PathVariable String keyword) {
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