package com.example.budgeKeemi.exception.handler;

import com.example.budgeKeemi.exception.excep.DuplicateCategoryBudgetException;
import com.example.budgeKeemi.exception.excep.InsufficientBalanceException;
import com.example.budgeKeemi.exception.excep.MultipartFileException;
import com.example.budgeKeemi.exception.excep.UnauthorizedException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

/*class 검색용
* org.springframework.web.multipart.support.MissingServletRequestPartException */

/*   @ExceptionHandler(Exception.class)
*    public ResponseEntity<String> exception(Exception e){
*        System.out.println(e.getClass().getName());
*        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("");
*
*    }*/

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<String> handleInsufficientBalanceException(InsufficientBalanceException e){
        //Bad Request 400
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<String> handleUnauthorizedException(UnauthorizedException e){
        //로그인 했지만 권한이 없는 경우 403
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }

    @ExceptionHandler(DuplicateCategoryBudgetException.class)
    public ResponseEntity<String> handleDuplicateCategoryBudgetException(DuplicateCategoryBudgetException e){
        //Bad Request 400
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleConstraintViolationException(MethodArgumentNotValidException e){

        Map<String, String> errors = getErrors(e);

        String errorMessage=errors.values().stream().collect(Collectors.joining("\n"));

        //Bad Request 400
        return ResponseEntity.badRequest().body(errorMessage);
    }

    @ExceptionHandler(MultipartFileException.class)
    public ResponseEntity<String> handleMultipartFileException(MultipartFileException e){
        //Bad Request 400
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    private static Map<String, String> getErrors(MethodArgumentNotValidException e) {
        Map<String,String> errors=new HashMap<>();

        e.getBindingResult().getFieldErrors().forEach(error->{
            String fieldName = error.getField();
            String defaultMessage = error.getDefaultMessage();
            errors.put(fieldName,defaultMessage);
        });
        return errors;
    }


}
