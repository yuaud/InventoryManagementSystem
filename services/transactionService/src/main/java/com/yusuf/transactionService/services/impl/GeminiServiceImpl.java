package com.yusuf.transactionService.services.impl;


import com.yusuf.transactionService.dtos.Response;
import com.yusuf.transactionService.services.GeminiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@Slf4j
public class GeminiServiceImpl implements GeminiService {

    @Value("${geminiApiUrl}")
    private String geminiApiUrl;

    @Value("${geminiApiKey}")
    private String geminiApiKey;

    private final WebClient webClient;

    public GeminiServiceImpl(WebClient.Builder webClient) {
        this.webClient = webClient.build();
    }


    @Override
    public Response getChatAnswer(String message) {
        // Construct the request payload
        Map<String, Object> requestBody = Map.of(
                "contents", new Object[] {
                        Map.of("parts", new Object[] {
                                Map.of("text", message)
                        })
                }
        );

        // Make API Call
        String response = webClient.post()
                .uri(geminiApiUrl + geminiApiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return Response.builder()
                .status(200)
                .message(response)
                .build();
    }
}
