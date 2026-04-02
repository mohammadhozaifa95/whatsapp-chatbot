package com.chatbot.service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.chatbot.model.ReplyRule;

@Service
public class ChatbotService {

	private Logger logger = LoggerFactory.getLogger(ChatbotService.class);

	private final Map<String, ReplyRule> replyRules = new ConcurrentHashMap<>();
	private final Map<String, String> defaultReplies = new HashMap<>();
	private final Map<String, List<String>> userHistory = new ConcurrentHashMap<>();
	private final Random random = new Random();

	private final List<String> fallbacks = Arrays.asList("Sorry, didn't get that", "Hmm? Say 'help'",
			"Not sure, try 'help'", "I don't understand");

	public ChatbotService() {
		initializeDefaultRules();
	}

	private void initializeDefaultRules() {
		try {
			defaultReplies.put("hi", "Hello! 👋");
			defaultReplies.put("hello", "Hi there!");
			defaultReplies.put("bye", "Goodbye! Have a nice day!");
			defaultReplies.put("good morning", "Good Morning! ☀️");
			defaultReplies.put("good night", "Good Night! 🌙");
			defaultReplies.put("help", "Try: hi, hello, bye, good morning, good night");
			defaultReplies.put("status", "I'm running perfectly!");

			for (Map.Entry<String, String> entry : defaultReplies.entrySet()) {
				ReplyRule rule = new ReplyRule(entry.getKey(), entry.getValue(), false, System.currentTimeMillis());
				replyRules.put(entry.getKey().toLowerCase(), rule);
			}

			logger.info("Loaded {} default rules", defaultReplies.size());
		} catch (Exception e) {
			logger.error("Failed to initialize rules: " + e.getMessage());
		}
	}

	public boolean addRule(String keyword, String response) {
		try {
			if (keyword == null || response == null) {
				logger.warn("Cannot add null rule");
				return false;
			}

			String key = keyword.toLowerCase().trim();

			if (defaultReplies.containsKey(key)) {
				logger.warn("Cannot override default rule: {}", keyword);
				return false;
			}

			replyRules.put(key, new ReplyRule(keyword, response, false, System.currentTimeMillis()));
			logger.info("Added rule: {} -> {}", keyword, response);
			return true;

		} catch (Exception e) {
			logger.error("Error adding rule: " + e.getMessage());
			return false;
		}
	}

	public boolean removeRule(String keyword) {
		try {
			if (keyword == null)
				return false;

			String key = keyword.toLowerCase().trim();

			if (defaultReplies.containsKey(key)) {
				logger.warn("Cannot remove default rule: {}", keyword);
				return false;
			}

			ReplyRule removed = replyRules.remove(key);

			if (removed != null) {
				logger.info("Removed rule: {}", keyword);
				return true;
			}

			logger.warn("Rule not found: {}", keyword);
			return false;

		} catch (Exception e) {
			logger.error("Error removing rule: " + e.getMessage());
			return false;
		}
	}

	public Map<String, Object> getAllRules() {
		try {
			Map<String, Object> result = new HashMap<>();
			result.put("total", replyRules.size());
			result.put("rules", replyRules);
			return result;
		} catch (Exception e) {
			logger.error("Error fetching rules: " + e.getMessage());
			Map<String, Object> error = new HashMap<>();
			error.put("error", "Failed to fetch rules");
			return error;
		}
	}

	public String getReply(String message, String userId) {
		try {
			// Save history
			userHistory.computeIfAbsent(userId, k -> new ArrayList<>()).add(message);

			if (message == null || message.trim().isEmpty()) {
				return fallbacks.get(0);
			}

			String msg = message.toLowerCase().trim();

			// Handle special commands
			if (msg.equals("history")) {
				return getHistory(userId);
			}

			if (msg.equals("rules") || msg.equals("commands")) {
				return getAllRulesText();
			}

			// HashMap lookup - O(1)
			ReplyRule rule = replyRules.get(msg);
			if (rule != null) {
				logger.info("Match: {} -> {}", msg, rule.getResponse());
				return rule.getResponse();
			}

			// Contains match
			for (ReplyRule r : replyRules.values()) {
				try {
					if (msg.contains(r.getKeyword().toLowerCase())) {
						logger.info("Contains: {} in {}", r.getKeyword(), msg);
						return r.getResponse();
					}
				} catch (Exception e) {
					logger.warn("Error in contains match: " + e.getMessage());
				}
			}

			logger.info("No match: {}", msg);
			return fallbacks.get(random.nextInt(fallbacks.size()));

		} catch (Exception e) {
			logger.error("Error getting reply: " + e.getMessage());
			return "Sorry, something went wrong. Please try again.";
		}
	}

	private String getHistory(String userId) {
		try {
			List<String> history = userHistory.get(userId);
			if (history == null || history.isEmpty()) {
				return "No messages yet";
			}

			StringBuilder sb = new StringBuilder("Your last messages:\n");
			sb.append("-------------------\n");

			int start = Math.max(0, history.size() - 10);
			for (int i = start; i < history.size(); i++) {
				sb.append(i + 1).append(". ").append(history.get(i)).append("\n");
			}

			return sb.toString();
		} catch (Exception e) {
			logger.error("Error getting history: " + e.getMessage());
			return "Failed to load history";
		}
	}

	private String getAllRulesText() {
		try {
			if (replyRules.isEmpty()) {
				return "No rules available";
			}

			StringBuilder sb = new StringBuilder("Available commands:\n");
			sb.append("------------------\n");

			int count = 0;
			for (ReplyRule rule : replyRules.values()) {
				if (count >= 15) {
					sb.append("... and ").append(replyRules.size() - 15).append(" more");
					break;
				}
				sb.append("• ").append(rule.getKeyword()).append("\n");
				count++;
			}

			return sb.toString();
		} catch (Exception e) {
			return "Failed to load commands";
		}
	}
}