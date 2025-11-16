package com.example.todo.application.member.dto;

import com.example.todo.domain.member.Member;
import com.example.todo.domain.member.enumerate.MemberRole;

public record MemberResponse(
        Long memberId,
                            String memberName,
                            MemberRole memberRole
) {
    public static MemberResponse from(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getName(),
                member.getRole()
        );
    }
}