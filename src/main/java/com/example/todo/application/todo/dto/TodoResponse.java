package com.example.todo.application.todo.dto;

import com.example.todo.domain.todo.Todo;
import com.example.todo.domain.todo.enumerate.TodoStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public record TodoResponse(
        Long todoId,
        Long memberId,
        List<String> tags,
        LocalDate endAt,
        Long restDate,
        String content,
        Long seq,
        TodoStatus status
) {
    public static TodoResponse from(Todo todo, Long restDate) {
        return new TodoResponse(
                todo.getId(),
                todo.getMember().getId(),
                todo.getTags() == null ? List.of() : new ArrayList<>(todo.getTags()),
                todo.getEndAt(),
                restDate,
                todo.getContent(),
                todo.getSeq(),
                todo.getStatus()
        );
    }
}
