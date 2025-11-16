package com.example.todo.application.todo.provided;

import com.example.todo.application.todo.dto.TodoResponse;
import com.example.todo.application.todo.dto.TodoSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TodoFinder {
    Page<TodoResponse> getMyAllTodos(Long memberId, Pageable pageable);
    Page<TodoResponse> getMyDeletedTodos(Long memberId, Pageable pageable);
    Page<TodoResponse> searchTodos(TodoSearchCondition condition, Pageable pageable);
    TodoResponse findById(Long id);
}