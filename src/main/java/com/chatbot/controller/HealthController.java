package com.chatbot.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    
    /**
     * This endpoint is created for Render free tier keep-alive.
     * 
     * PROBLEM:
     * Render free tier puts your app to sleep after 15 minutes of inactivity.
     * When the app sleeps, the first request takes 30-50 seconds to wake up.
     * 
     * SOLUTION:
     * UptimeRobot (free service) calls this endpoint every 5 minutes.
     * This keeps the app active and prevents it from sleeping.
     * 
     * WHAT IT DOES:
     * Simply returns "pong" - no processing, no database, no heavy logic.
     * Just a lightweight response to confirm the app is alive.
     * 
     * @return "pong" - signals that the app is running and healthy
     */
	 @GetMapping("/")
	    public String home() {
	        return "✅ WhatsApp Chatbot is active!";
	    }
}