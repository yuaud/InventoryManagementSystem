package com.yusuf.transactionService.exceptions;

public class NameValueRequiredException extends RuntimeException {
    public NameValueRequiredException(String message) {
        super(message);
    }
}
