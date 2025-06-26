package com.yusuf.transactionService.services;

import com.yusuf.transactionService.dtos.Response;
import com.yusuf.transactionService.dtos.TransactionRequest;
import com.yusuf.transactionService.enums.TransactionStatus;

public interface TransactionService {
    Response purchase(TransactionRequest transactionRequest);
    Response sell(TransactionRequest transactionRequest);
    Response returnToSupplier(TransactionRequest transactionRequest);
    Response getAllTransactions(int page, int size, String filter);
    Response getTransactionById(Long id);
    Response getAllTransactionByMonthAndYear(int month, int year);
    Response updateTransaction(Long transactionId, TransactionStatus status);
}
