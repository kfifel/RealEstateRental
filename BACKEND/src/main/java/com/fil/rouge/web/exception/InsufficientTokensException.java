package com.fil.rouge.web.exception;

public class InsufficientTokensException extends RuntimeException {
    public InsufficientTokensException(String s) {
        super(s);
    }

    public InsufficientTokensException(String message, Throwable cause) {
        super(message, cause);
    }
}
