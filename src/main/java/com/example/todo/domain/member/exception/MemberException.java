package com.example.todo.domain.member.exception;

import com.example.todo.domain.global.apiPayload.exception.ErrorType;
import com.example.todo.domain.global.apiPayload.exception.GlobalException;

public class MemberException extends GlobalException {

    public MemberException(ErrorType errorType) {
        super(errorType);
    }
}