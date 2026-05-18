package com.helpdeskchatbot.helpdeskchat.service;

import com.helpdeskchatbot.helpdeskchat.tools.ticketTools;
import org.springframework.core.io.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
//@NoArgsConstructor
//@RequiredArgsConstructor
public class AiService {

    private final ChatClient chatClient;
    private final ticketTools  ticketTools;

    @Value("classpath:/promt.st")
    private Resource sysPromtsResource;

    public AiService(ChatClient chatClient, ticketTools ticketTools) {
        this.chatClient = chatClient;
        this.ticketTools = ticketTools;
    }

    public String getAiResponse(String userMessage) {
        return chatClient.prompt()
                .tools(ticketTools)
                .system(sysPromtsResource)
                .user(userMessage)
                .call()
                .content();
    }

}
