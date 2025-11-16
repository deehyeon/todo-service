package com.example.todo.adapter.member.dto;

import com.example.todo.domain.member.dto.TokenInfo;

public record MemberLoginInfo(
        TokenInfo tokenInfo,

        MemberResponse memberInfo
) {
    public static MemberLoginInfo from(TokenInfo tokenInfo, MemberResponse memberResponse) {
        return new MemberLoginInfo(tokenInfo, memberResponse);
    }
}
