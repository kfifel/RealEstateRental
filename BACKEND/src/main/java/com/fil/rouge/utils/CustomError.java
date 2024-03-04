package com.fil.rouge.utils;

import lombok.*;


@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class CustomError {
    private final String field;
    private final String message;
}
