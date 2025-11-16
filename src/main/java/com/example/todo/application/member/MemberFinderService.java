package com.example.todo.application.member;

import com.example.todo.application.member.provided.MemberFinder;
import com.example.todo.application.member.required.MemberRepository;
import com.example.todo.domain.global.vo.Email;
import com.example.todo.domain.member.Member;
import com.example.todo.domain.member.exception.MemberErrorType;
import com.example.todo.domain.member.exception.MemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberFinderService implements MemberFinder {
    private final MemberRepository memberRepository;

    @Override
    public Member findByEmail(Email email) {
        return memberRepository.findByEmail(email).orElseThrow(() -> new MemberException(MemberErrorType.INVALID_EMAIL));
    }

    @Override
    public Member findById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new MemberException(MemberErrorType.MEMBER_NOT_FOUND));
    }
}
