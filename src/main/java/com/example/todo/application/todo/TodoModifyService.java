package com.example.todo.application.todo;

import com.example.todo.application.member.provided.MemberFinder;
import com.example.todo.application.todo.provided.TodoSaver;
import com.example.todo.application.todo.required.TodoRepository;
import com.example.todo.config.todo.TodoPermissionChecker;
import com.example.todo.domain.member.Member;
import com.example.todo.domain.todo.Todo;
import com.example.todo.application.todo.dto.TodoCreateRequest;
import com.example.todo.application.todo.dto.TodoUpdateRequest;
import com.example.todo.domain.todo.exception.TodoErrorType;
import com.example.todo.domain.todo.exception.TodoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TodoModifyService implements TodoSaver {
    private final MemberFinder memberFinder;
    private final TodoRepository todoRepository;
    private final TodoPermissionChecker todoPermissionChecker;

    @Override
    public Long save(Long memberId, TodoCreateRequest request) {
        Member member = memberFinder.findById(memberId);

        Long lastSeq = todoRepository.findLastSeqByMemberId(member.getId());
        Long nextSeq = lastSeq + 1;

        Todo todo = Todo.create(member, request.tags(), request.endAt(), request.content(), nextSeq);
        Todo saved = todoRepository.save(todo);

        return saved.getId();
    }

    @Override
    public void update(Long todoId, Long memberId, TodoUpdateRequest request) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new TodoException(TodoErrorType.TODO_NOT_FOUND));

        Member member = memberFinder.findById(memberId);

        todoPermissionChecker.checkCanModifyOrDelete(member, todo);

        // 2) 태그 수정
        if (request.tags() != null) {
            todo.clearTags();
            todo.addTags(request.tags());
        }

        // 3) 종료일 수정
        if (request.endAt() != null) {
            todo.modifyEndAt(request.endAt());
        }

        // 4) 내용 수정
        if (request.content() != null) {
            todo.modifyContent(request.content());
        }

        // 5) 상태(status) 수정
        if (request.status() != null) {
            todo.modifyStatus(request.status());
        }

        // 6) seq(순서) 수정 — 중요
        if (request.seq() != null && !request.seq().equals(todo.getSeq())) {
            updateTodoSequence(todo, request.seq());   // 별도 메서드로 분리
        }
    }

    @Override
    public void delete(Long todoId, Long memberId) {
        Todo todo = todoRepository.findById(todoId).orElseThrow(() -> new TodoException(TodoErrorType.TODO_NOT_FOUND));
        Member member = memberFinder.findById(memberId);

        todoPermissionChecker.checkCanModifyOrDelete(member, todo);

        todo.softDelete();
    }

    @Override
    public void restore(Long todoId, Long memberId) {
        Todo todo = todoRepository.findByIdAndMember_IdAndIsDeletedTrue(todoId, memberId)
                .orElseThrow(() -> new TodoException(TodoErrorType.TODO_NOT_FOUND));

        todo.restore();
    }

    private void updateTodoSequence(Todo target, Long newSeq) {
        Long memberId = target.getMember().getId();
        Long oldSeq = target.getSeq();

        // 1) 현재 멤버의 TODO 조회
        List<Todo> todos = todoRepository.findAllByMemberIdOrderBySeq(memberId);

        // 2) shift 처리
        if (newSeq < oldSeq) {
            for (Todo t : todos) {
                if (t.getSeq() >= newSeq && t.getSeq() < oldSeq) {
                    t.modifySeq(t.getSeq() + 1);
                }
            }
        } else {
            for (Todo t : todos) {
                if (t.getSeq() > oldSeq && t.getSeq() <= newSeq) {
                    t.modifySeq(t.getSeq() - 1);
                }
            }
        }
        target.modifySeq(newSeq);
    }
}