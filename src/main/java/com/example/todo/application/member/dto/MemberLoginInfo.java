package com.example.todo.application.member.dto;

public record MemberLoginInfo(
        TokenInfo tokenInfo,

        MemberResponse memberInfo
) {
    public static MemberLoginInfo from(TokenInfo tokenInfo, MemberResponse memberResponse) {
        return new MemberLoginInfo(tokenInfo, memberResponse);
    }
}
