package com.example.todo.application.member;

import com.example.todo.application.member.required.MemberRepository;
import com.example.todo.domain.global.vo.Email;
import com.example.todo.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${todo.admin.name}")
    private String adminName;

    @Value("${todo.admin.email}")
    private String adminEmail;

    @Value("${todo.admin.password}")
    private String adminPassword;

    @Override
    public void run(String... args) {
        if (memberRepository.existsByEmail(new Email(adminEmail))) {
            return;
        }

        Member admin = Member.createAdmin(
                adminName,
                adminEmail,
                passwordEncoder.encode(adminPassword)
        );

        memberRepository.save(admin);
    }
}