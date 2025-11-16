package com.example.todo.application.todo;

import com.example.todo.application.member.provided.MemberFinder;
import com.example.todo.application.todo.dto.TodoResponse;
import com.example.todo.application.todo.dto.TodoSearchCondition;
import com.example.todo.application.todo.provided.TodoFinder;
import com.example.todo.application.todo.required.TodoRepository;
import com.example.todo.domain.todo.Todo;
import com.example.todo.domain.todo.exception.TodoErrorType;
import com.example.todo.domain.todo.exception.TodoException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TodoFinderService implements TodoFinder {
    private final TodoRepository todoRepository;

    @Override
    public Page<TodoResponse> getAllTodos(Pageable pageable) {
        return searchTodos(new TodoSearchCondition(null, null, null), pageable);
    }

    @Override
    public Page<TodoResponse> getMyAllTodos(Long memberId, Pageable pageable) {
        return searchTodos(new TodoSearchCondition(memberId, null, null), pageable);
    }

    @Override
    public Page<TodoResponse> getMyDeletedTodos(Long memberId, Pageable pageable) {
        Page<Todo> todos = todoRepository.findByMember_IdAndIsDeletedTrue(memberId, pageable);

        return todos.map(todo -> {
            Long restDate = calculateRestDate(todo.getEndAt());
            return TodoResponse.from(todo, restDate);
        });
    }

    @Override
    public Page<TodoResponse> searchTodos(TodoSearchCondition condition, Pageable pageable) {
        Page<Todo> todos = todoRepository.searchTodos(condition, pageable);

        return todos.map(todo -> {
            Long restDate = calculateRestDate(todo.getEndAt());
            return TodoResponse.from(todo, restDate);
        });
    }

    @Override
    public TodoResponse findById(Long id) {
        Todo todo = todoRepository.findByIdAndIsDeletedFalse(id).orElseThrow(() -> new TodoException(TodoErrorType.TODO_NOT_FOUND));
        long restDate = calculateRestDate(todo.getEndAt());
        return TodoResponse.from(todo, restDate);
    }

    private long calculateRestDate(LocalDate endAt) {
        LocalDate today = LocalDate.now();
        return ChronoUnit.DAYS.between(today, endAt);
    }
}