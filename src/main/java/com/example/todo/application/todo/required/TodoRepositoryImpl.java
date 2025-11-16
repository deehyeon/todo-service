package com.example.todo.application.todo.required;

import com.example.todo.application.todo.dto.TodoSearchCondition;
import com.example.todo.domain.todo.QTodo;
import com.example.todo.domain.todo.Todo;
import com.example.todo.domain.todo.enumerate.TodoStatus;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

@RequiredArgsConstructor
public class TodoRepositoryImpl implements TodoRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Todo> searchTodos(TodoSearchCondition searchCondition,
                                  Pageable pageable) {

        QTodo todo = QTodo.todo;

        List<Todo> results = queryFactory
                .selectFrom(todo)
                .where(
                        isNotDeleted(todo),
                        memberEq(searchCondition.memberId(), todo),
                        tagIn(searchCondition.tags(), todo),
                        statusEq(searchCondition.status(), todo)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(todo.seq.desc())   // 사용자가 생성한 seq 기준 정렬
                .fetch();

        long total = queryFactory
                .select(todo.count())
                .from(todo)
                .where(
                        isNotDeleted(todo),
                        memberEq(searchCondition.memberId(), todo),
                        tagIn(searchCondition.tags(), todo),
                        statusEq(searchCondition.status(), todo)
                )
                .fetchOne();

        return PageableExecutionUtils.getPage(results, pageable, () -> total);
    }

    private BooleanExpression isNotDeleted(QTodo todo) {
        return todo.isDeleted.eq(false);
    }

    private BooleanExpression memberEq(Long memberId, QTodo todo) {
        return memberId != null ? todo.member.id.eq(memberId) : null;
    }

    private BooleanExpression tagIn(List<String> tags, QTodo todo) {
        if (tags == null || tags.isEmpty()) {
            return null;
        }
        return todo.tags.any().in(tags);
    }

    private BooleanExpression statusEq(TodoStatus status, QTodo todo) {
        return status != null ? todo.status.eq(status) : null;
    }
}