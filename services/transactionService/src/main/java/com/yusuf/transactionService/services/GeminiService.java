package com.yusuf.transactionService.services;

import com.yusuf.transactionService.dtos.Response;

public interface GeminiService {
    Response getChatAnswer(String message);
}
