package com.example.todo.application.member.provided;

import com.example.todo.adapter.member.dto.MemberLoginInfo;
import com.example.todo.domain.member.dto.MemberLoginRequest;
import com.example.todo.domain.member.dto.MemberSignupRequest;
import com.example.todo.domain.member.dto.TokenResponse;

public interface Auth {
    MemberLoginInfo signup(MemberSignupRequest memberSignupRequest);
    MemberLoginInfo login(MemberLoginRequest memberLoginRequest);
    TokenResponse refresh(String refreshToken);
}