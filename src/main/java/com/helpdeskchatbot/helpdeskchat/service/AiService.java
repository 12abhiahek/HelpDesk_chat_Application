package com.helpdeskchatbot.helpdeskchat.service;

import com.helpdeskchatbot.helpdeskchat.tools.EmailTools;
import com.helpdeskchatbot.helpdeskchat.tools.ticketTools;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.core.io.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
//@NoArgsConstructor
//@RequiredArgsConstructor
public class AiService {

    private final ChatClient chatClient;
    private final ticketTools  ticketTools;
    private final EmailTools emailTools;

    @Value("classpath:/promt.st")
    private Resource sysPromtsResource;

    public AiService(ChatClient chatClient, ticketTools ticketTools, EmailTools emailTools) {
        this.chatClient = chatClient;
        this.ticketTools = ticketTools;
        this.emailTools = emailTools;
    }

    public String getAiResponse(String userMessage,String conversationId) {
        return chatClient.prompt()
                .advisors(advisorSpec ->advisorSpec.param(ChatMemory.CONVERSATION_ID, conversationId))
                .tools(ticketTools)
                .system(sysPromtsResource)
                .user(userMessage)
                .call()
                .content();
    }

    public Flux<String> streamAiResponse(String userMessage, String conversationId) {
       return chatClient.prompt()
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, conversationId))
                .tools(ticketTools)
                .system(sysPromtsResource)
                .user(userMessage)
                .stream()
               .content();
    }

}
