package com.example.todo.config.todo;

import com.example.todo.domain.member.Member;
import com.example.todo.domain.member.enumerate.MemberRole;
import com.example.todo.domain.todo.Todo;
import com.example.todo.domain.todo.exception.TodoErrorType;
import com.example.todo.domain.todo.exception.TodoException;
import org.springframework.stereotype.Component;

@Component
public class TodoPermissionChecker {
    private final TodoPolicyProperties policyProperties;

    public TodoPermissionChecker(TodoPolicyProperties policyProperties) {
        this.policyProperties = policyProperties;
    }

    public void checkCanModifyOrDelete(Member member, Todo todo) {
        if (!canModifyOrDelete(member, todo)) {
            throw new TodoException(TodoErrorType.UNAUTHORIZED_MEMBER_ACCESS);
        }
    }

    public boolean canModifyOrDelete(Member member, Todo todo) {
        boolean isAdmin = member.getRole() == MemberRole.ADMIN;

        if (!isAdmin) {return todo.getMember().equals(member);}

        return switch (policyProperties.getAdminPolicy()) {
            case MANAGE_ALL      -> true;
            case MANAGE_OWN_ONLY -> todo.getMember().equals(member);
        };
    }
}