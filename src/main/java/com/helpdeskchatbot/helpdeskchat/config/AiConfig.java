package com.helpdeskchatbot.helpdeskchat.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    private Logger logger = LoggerFactory.getLogger(AiConfig.class);


    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder, ChatMemory  chatMemory) {

        logger.debug("ChatClient created");
        logger.info("Initializing ChatClient with chat memory {}",chatMemory.getClass().getName());
        return chatClientBuilder
                .defaultAdvisors(new SimpleLoggerAdvisor(),
                        MessageChatMemoryAdvisor
                                .builder(chatMemory)
                                .build())
                .build();
    }
}
