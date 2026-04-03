package com.chatbot.utils;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("WhatsApp Chatbot API")
                        .version("1.0")
                        .description("""
                                 WhatsApp Chatbot Backend Simulation
                                
                                This API simulates a WhatsApp chatbot backend using Spring Boot.
                                
                                Features:
                                - Receive WhatsApp messages via webhook
                                - Predefined replies (hi → Hello, bye → Goodbye)
                                - Dynamic rule management (add/delete replies without restart)
                                - Message logging
                                - Health check endpoint for Render keep-alive
                                
                                Tech Stack:
                                - Java 17
                                - Spring Boot 3.2.4
                                - ConcurrentHashMap for thread-safe storage
                                - Render for deployment
                                """)
                        .contact(new Contact()
                                .name("Mohammad Hozaifa")
                                .email("khanmohdhozaifa@gmail.com")
                                .url("https://github.com/mohammadhozaifa95"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Local Development Server"),
                        new Server()
                                .url("https://whatsapp-chatbot.onrender.com")
                                .description("Production Server (Render)")
                ));
    }
}