package com.helpdeskchatbot.helpdeskchat.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
//@NoArgsConstructor
//@RequiredArgsConstructor
public class AiService {

    private final ChatClient chatClient;

    public AiService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String getAiResponse(String userMessage) {
        return chatClient.prompt()
                .user(userMessage)
                .call()
                .content();
    }

}
