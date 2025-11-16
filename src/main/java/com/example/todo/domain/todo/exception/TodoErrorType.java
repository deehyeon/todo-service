package com.example.todo.domain.todo.exception;

import com.example.todo.domain.global.apiPayload.exception.ErrorType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum TodoErrorType implements ErrorType {
    TODO_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 TODO 입니다."),
    UNAUTHORIZED_MEMBER_ACCESS(HttpStatus.UNAUTHORIZED, "권한이 없는 사용자입니다.")
    ;

    private final HttpStatus status;

    private final String message;
}
