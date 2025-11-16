package com.example.todo.application.todo.required;

import com.example.todo.application.todo.dto.TodoSearchCondition;
import com.example.todo.domain.todo.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TodoRepositoryCustom {
    Page<Todo> searchTodos(TodoSearchCondition searchCondition,
                           Pageable pageable);
}