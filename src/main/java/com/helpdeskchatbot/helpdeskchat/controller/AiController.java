package com.helpdeskchatbot.helpdeskchat.controller;

import com.helpdeskchatbot.helpdeskchat.service.AiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ai")
public class AiController {

    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/query")
    public ResponseEntity<String> sendMessage(@RequestBody String userMessage){
        String response = aiService.getAiResponse(userMessage);
        return ResponseEntity.ok(response);
    }

}
