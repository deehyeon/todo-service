package com.example.todo.application.member.provided;

import com.example.todo.domain.global.vo.Email;
import com.example.todo.domain.member.Member;

import java.util.Optional;

public interface MemberFinder {
    Member findByEmail(Email email);
    Member findById(Long id);
}
