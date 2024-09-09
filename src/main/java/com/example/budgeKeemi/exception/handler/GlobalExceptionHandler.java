package com.example.budgeKeemi.exception.handler;

import com.example.budgeKeemi.exception.excep.InsufficientBalanceException;
import com.example.budgeKeemi.exception.excep.UnauthorizedException;
import org.springframework.http.HttpStatus;
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

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<String> handleUnauthorizedException(UnauthorizedException e){
        //로그인 했지만 권한이 없는 경우
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }
}
