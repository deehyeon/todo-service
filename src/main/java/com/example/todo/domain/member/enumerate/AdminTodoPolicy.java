package com.example.todo.domain.member.enumerate;

public enum AdminTodoPolicy {
    MANAGE_ALL,       // 1. 관리자는 모든 사용자의 ToDo 수정/삭제 가능
    MANAGE_OWN_ONLY   // 2. 관리자는 본인이 생성한 ToDo만 수정/삭제 가능
}
