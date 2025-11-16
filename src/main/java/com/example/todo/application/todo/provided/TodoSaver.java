package com.example.todo.application.todo.provided;

import com.example.todo.domain.todo.dto.TodoCreateRequest;
import com.example.todo.domain.todo.dto.TodoUpdateRequest;

public interface TodoSaver {
    Long save(Long memberId, TodoCreateRequest request);
    void update(Long todoId, Long memberId, TodoUpdateRequest request);
    void delete(Long todoId, Long memberId);
    void restore(Long todoId, Long memberId);
}
