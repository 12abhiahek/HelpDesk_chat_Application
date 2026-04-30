package com.helpdeskchatbot.helpdeskchat.service;

import com.helpdeskchatbot.helpdeskchat.tools.ticketTools;
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
    private final ticketTools  ticketTools;

    public AiService(ChatClient chatClient, ticketTools ticketTools) {
        this.chatClient = chatClient;
        this.ticketTools = ticketTools;
    }

    public String getAiResponse(String userMessage) {
        return chatClient.prompt()
                .tools(ticketTools)
                .user(userMessage)
                .call()
                .content();
    }

}
