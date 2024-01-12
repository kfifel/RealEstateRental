package com.fil.rouge.web.exception;

public class EmailOrPasswordIncorrectException extends RuntimeException {

    public EmailOrPasswordIncorrectException(String message) {
        super(message);
    }
}
