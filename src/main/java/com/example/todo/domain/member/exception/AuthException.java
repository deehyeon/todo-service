package com.example.todo.domain.member.exception;

import com.example.todo.domain.global.apiPayload.exception.ErrorType;
import com.example.todo.domain.global.apiPayload.exception.GlobalException;

public class AuthException extends GlobalException {
    public AuthException(ErrorType errorType) {
        super(errorType);
    }
}
