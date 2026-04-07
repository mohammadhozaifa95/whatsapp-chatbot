package com.chatbot.utils;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
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
                        .description("WhatsApp Chatbot Backend using Spring Boot")
                        .contact(new Contact()
                                .name("Mohammad Hozaifa")
                                .email("khanmohdhozaifa@gmail.com")
                                .url("https://github.com/mohammadhozaifa95")))
                .servers(List.of(
                        new Server()
                                .url("/")   // 🔥 IMPORTANT (auto-detect same domain)
                                .description("Default Server"),

                        new Server()
                                .url("https://whatsapp-chatbot-zlrx.onrender.com")
                                .description("Production Server")
                ));
    }
}