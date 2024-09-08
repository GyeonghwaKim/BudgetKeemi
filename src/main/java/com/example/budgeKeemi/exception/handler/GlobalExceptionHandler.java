package com.example.budgeKeemi.exception.handler;

import com.example.budgeKeemi.exception.excep.InsufficientBalanceException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<String> handleInsufficientBalanceException(InsufficientBalanceException e){
        //Bad Request 400
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
