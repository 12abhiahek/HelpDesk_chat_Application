package com.helpdeskchatbot.helpdeskchat.controller;

import com.helpdeskchatbot.helpdeskchat.service.AiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/ai")
public class AiController {

    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/query")
    public ResponseEntity<String> sendMessage(@RequestBody String userMessage,@RequestHeader("ConversationId") String conversationId){
        String response = aiService.getAiResponse(userMessage,conversationId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/stream")
    public Flux<String> streamResponse(@RequestBody String userMessage , @RequestHeader("conversationId") String conversationId){
        return this.aiService.streamAiResponse(userMessage, conversationId);
    }

}
