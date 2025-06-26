package com.yusuf.transactionService.controllers;


import com.yusuf.transactionService.dtos.Response;
import com.yusuf.transactionService.services.GeminiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gemini")
@RequiredArgsConstructor
public class GeminiController {

    private final GeminiService geminiService;

    @PostMapping("/chat")
    public ResponseEntity<Response> getChat(
            @RequestBody String message
    ) {
        return ResponseEntity.ok(geminiService.getChatAnswer(message));
    }

}
