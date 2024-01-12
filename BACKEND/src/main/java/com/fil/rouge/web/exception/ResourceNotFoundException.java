package com.fil.rouge.web.exception;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends Exception{

    private final String field;

    public ResourceNotFoundException(String field, String message) {
        super(message);
        this.field = field;
    }

}
