package com.fil.rouge.aop.handler;

import com.fil.rouge.utils.CustomError;
import com.fil.rouge.utils.ResponseApi;
import com.fil.rouge.utils.ValidationException;
import com.fil.rouge.web.exception.InsufficientTokensException;
import com.fil.rouge.web.exception.PropertyExistsException;
import com.fil.rouge.web.exception.ResourceNotFoundException;
import com.fil.rouge.web.exception.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class AppExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseApi<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        ResponseApi<Object> response = new ResponseApi<>();
        List<CustomError> errorList = new ArrayList<>();
        response.setMessage("Validation error");
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errorList.add(new CustomError(fieldName, errorMessage));
        });
        response.setErrors(errorList);
        return response;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ResourceNotFoundException.class)
    private ResponseApi<Object> handleValidationExceptions(ResourceNotFoundException ex) {
        ResponseApi<Object> response = new ResponseApi<>();
        List<CustomError> errorList = new ArrayList<>();
        response.setMessage("Resource not found");
        errorList.add(CustomError.builder()
                .field(ex.getField())
                .message(ex.getMessage())
                .build());
        response.setErrors(errorList);
        return response;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    private ResponseApi<Object> handleValidationExceptions(ValidationException ex) {
        ResponseApi<Object> response = new ResponseApi<>();
        List<CustomError> errorList = new ArrayList<>();
        response.setMessage("Validation error");
        errorList.add(CustomError.builder()
                .field(ex.getCustomError().getField())
                .message(ex.getCustomError().getMessage())
                .build());
        response.setErrors(errorList);
        return response;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    private ResponseEntity<ResponseApi<Object>> handleValidationExceptions(Exception ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.internalServerError().body(ResponseApi.builder()
                .message("Internal server error")
                        .result(ex.getMessage())
                .build());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({UnauthorizedException.class, InsufficientTokensException.class, BadCredentialsException.class})
    public ResponseEntity<ResponseApi<Object>> handleUnauthorizedException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseApi.builder()
                .message(ex.getMessage())
                .build());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({IllegalArgumentException.class, PropertyExistsException.class})
    public ResponseEntity<Object> handleIllegalArgumentException(RuntimeException ex) {
        HashMap<String, String> error = new HashMap<>();
        error.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(error);
    }
}
