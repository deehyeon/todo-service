package com.example.todo.domain.todo.exception;

import com.example.todo.domain.global.apiPayload.exception.ErrorType;
import com.example.todo.domain.global.apiPayload.exception.GlobalException;

public class TodoException extends GlobalException {

    public TodoException(ErrorType errorType) {
        super(errorType);
    }
}
