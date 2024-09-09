package com.example.budgeKeemi.exception.excep;
//로그인했지만 권한이 없는경우
public class UnauthorizedException extends RuntimeException{
    public UnauthorizedException(String message) {
        super(message);
    }
}
